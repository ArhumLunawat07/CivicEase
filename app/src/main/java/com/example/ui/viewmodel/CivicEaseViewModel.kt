package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.ChatMessage
import com.example.data.api.GeminiClient
import com.example.data.model.*
import com.example.data.repository.SchemeRepository
import com.example.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppNavScreen {
  LANDING,
  FINDER,
  RESULTS,
  SAVED
}

enum class SortOption(val label: String) {
  BY_RELEVANCE("Most Relevant"),
  BY_NAME("Scheme Name (A-Z)"),
  BY_CATEGORY("Category")
}

data class QuestionnaireUiState(
  val currentStep: Int = 1, // 1 to 5
  val answers: UserProfileAnswers = UserProfileAnswers(),
  val isSubmitted: Boolean = false,
  val stepValidationError: String? = null
)

data class FilterUiState(
  val searchQuery: String = "",
  val selectedCategory: SchemeCategory? = null,
  val selectedLevel: SchemeLevel? = null,
  val sortOption: SortOption = SortOption.BY_RELEVANCE
)

class CivicEaseViewModel(application: Application) : AndroidViewModel(application) {

  private val prefsRepo = UserPreferencesRepository(application)

  private val _currentScreen = MutableStateFlow(AppNavScreen.LANDING)
  val currentScreen: StateFlow<AppNavScreen> = _currentScreen.asStateFlow()

  private val _questionnaireState = MutableStateFlow(QuestionnaireUiState())
  val questionnaireState: StateFlow<QuestionnaireUiState> = _questionnaireState.asStateFlow()

  private val _filterState = MutableStateFlow(FilterUiState())
  val filterState: StateFlow<FilterUiState> = _filterState.asStateFlow()

  val savedSchemesState: StateFlow<Map<String, SavedSchemeState>> = prefsRepo.savedSchemesState

  private val _selectedSchemeForDetail = MutableStateFlow<Scheme?>(null)
  val selectedSchemeForDetail: StateFlow<Scheme?> = _selectedSchemeForDetail.asStateFlow()

  // AI Advisor State
  private val _isAdvisorOpen = MutableStateFlow(false)
  val isAdvisorOpen: StateFlow<Boolean> = _isAdvisorOpen.asStateFlow()

  private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
    listOf(
      ChatMessage(
        senderIsUser = false,
        text = "Namaste! I am your CivicEase Policy Advisor. Ask me anything about welfare schemes, required documents (like Income Certificates or DBT accounts), or how to apply on official portals."
      )
    )
  )
  val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

  private val _isAdvisorGenerating = MutableStateFlow(false)
  val isAdvisorGenerating: StateFlow<Boolean> = _isAdvisorGenerating.asStateFlow()

  // Google Sync State (Guest mode by default)
  private val _isGoogleConnected = MutableStateFlow(false)
  val isGoogleConnected: StateFlow<Boolean> = _isGoogleConnected.asStateFlow()
  private val _userAccountEmail = MutableStateFlow<String?>(null)
  val userAccountEmail: StateFlow<String?> = _userAccountEmail.asStateFlow()

  // Computed matching schemes
  val matchResults: StateFlow<List<SchemeMatchResult>> = combine(
    _questionnaireState,
    _filterState
  ) { qState, fState ->
    val baseMatches = SchemeRepository.evaluateMatches(qState.answers)
    var filtered = baseMatches

    // Search query filter
    if (fState.searchQuery.isNotBlank()) {
      val query = fState.searchQuery.trim().lowercase()
      filtered = filtered.filter { result ->
        result.scheme.name.lowercase().contains(query) ||
        result.scheme.shortCode.lowercase().contains(query) ||
        result.scheme.department.lowercase().contains(query) ||
        result.scheme.briefSummary.lowercase().contains(query)
      }
    }

    // Category filter
    if (fState.selectedCategory != null) {
      filtered = filtered.filter { it.scheme.category == fState.selectedCategory }
    }

    // Level filter
    if (fState.selectedLevel != null) {
      filtered = filtered.filter { it.scheme.level == fState.selectedLevel }
    }

    // Sort order
    when (fState.sortOption) {
      SortOption.BY_RELEVANCE -> filtered.sortedByDescending { it.rankingScore }
      SortOption.BY_NAME -> filtered.sortedBy { it.scheme.name }
      SortOption.BY_CATEGORY -> filtered.sortedBy { it.scheme.category.name }
    }
  }.stateIn(viewModelScope, SharingStarted.Lazily, SchemeRepository.evaluateMatches(UserProfileAnswers()))

  fun navigateTo(screen: AppNavScreen) {
    _currentScreen.value = screen
  }

  // Questionnaire actions
  fun updateStateOrUT(state: String) {
    val updated = _questionnaireState.value.answers.copy(stateOrUT = state)
    _questionnaireState.value = _questionnaireState.value.copy(answers = updated, stepValidationError = null)
  }

  fun updateAgeGroup(age: AgeGroup) {
    val updated = _questionnaireState.value.answers.copy(ageGroup = age)
    _questionnaireState.value = _questionnaireState.value.copy(answers = updated, stepValidationError = null)
  }

  fun updatePrimarySector(category: SchemeCategory) {
    val updated = _questionnaireState.value.answers.copy(primarySector = category)
    _questionnaireState.value = _questionnaireState.value.copy(answers = updated, stepValidationError = null)
  }

  fun updateOccupation(occupation: OccupationStatus) {
    val updated = _questionnaireState.value.answers.copy(occupationStatus = occupation)
    _questionnaireState.value = _questionnaireState.value.copy(answers = updated, stepValidationError = null)
  }

  fun updateIncomeBracket(income: IncomeBracket) {
    val updated = _questionnaireState.value.answers.copy(incomeBracket = income)
    _questionnaireState.value = _questionnaireState.value.copy(answers = updated, stepValidationError = null)
  }

  fun toggleSpecialCategory(cat: SpecialCategory) {
    val currentSet = _questionnaireState.value.answers.specialCategories.toMutableSet()
    if (currentSet.contains(cat)) currentSet.remove(cat) else currentSet.add(cat)
    val updated = _questionnaireState.value.answers.copy(specialCategories = currentSet)
    _questionnaireState.value = _questionnaireState.value.copy(answers = updated)
  }

  fun nextQuestionnaireStep() {
    val current = _questionnaireState.value.currentStep
    val answers = _questionnaireState.value.answers

    // Validation
    when (current) {
      1 -> {
        if (answers.stateOrUT.isBlank()) {
          _questionnaireState.value = _questionnaireState.value.copy(stepValidationError = "Please select your State or Union Territory")
          return
        }
        if (answers.ageGroup == null) {
          _questionnaireState.value = _questionnaireState.value.copy(stepValidationError = "Please select your age range")
          return
        }
      }
      2 -> {
        if (answers.primarySector == null) {
          _questionnaireState.value = _questionnaireState.value.copy(stepValidationError = "Please choose a primary reason or sector")
          return
        }
      }
      3 -> {
        if (answers.occupationStatus == null) {
          _questionnaireState.value = _questionnaireState.value.copy(stepValidationError = "Please select your employment or student status")
          return
        }
      }
      4 -> {
        if (answers.incomeBracket == null) {
          _questionnaireState.value = _questionnaireState.value.copy(stepValidationError = "Please select an income range (or 'Prefer not to say')")
          return
        }
      }
    }

    if (current < 5) {
      _questionnaireState.value = _questionnaireState.value.copy(currentStep = current + 1, stepValidationError = null)
    } else {
      // Step 5 is review, submit to results
      _questionnaireState.value = _questionnaireState.value.copy(isSubmitted = true)
      _currentScreen.value = AppNavScreen.RESULTS
    }
  }

  fun previousQuestionnaireStep() {
    val current = _questionnaireState.value.currentStep
    if (current > 1) {
      _questionnaireState.value = _questionnaireState.value.copy(currentStep = current - 1, stepValidationError = null)
    } else {
      _currentScreen.value = AppNavScreen.LANDING
    }
  }

  fun jumpToStep(step: Int) {
    _questionnaireState.value = _questionnaireState.value.copy(currentStep = step, stepValidationError = null)
    _currentScreen.value = AppNavScreen.FINDER
  }

  fun resetQuestionnaire() {
    _questionnaireState.value = QuestionnaireUiState()
    _filterState.value = FilterUiState()
  }

  // Filter actions
  fun updateSearchQuery(query: String) {
    _filterState.value = _filterState.value.copy(searchQuery = query)
  }

  fun selectCategoryFilter(category: SchemeCategory?) {
    _filterState.value = _filterState.value.copy(
      selectedCategory = if (_filterState.value.selectedCategory == category) null else category
    )
  }

  fun selectLevelFilter(level: SchemeLevel?) {
    _filterState.value = _filterState.value.copy(
      selectedLevel = if (_filterState.value.selectedLevel == level) null else level
    )
  }

  fun updateSortOption(sort: SortOption) {
    _filterState.value = _filterState.value.copy(sortOption = sort)
  }

  // Scheme detail bottom sheet
  fun openSchemeDetail(scheme: Scheme) {
    _selectedSchemeForDetail.value = scheme
  }

  fun closeSchemeDetail() {
    _selectedSchemeForDetail.value = null
  }

  // Bookmarking & Checklists
  fun toggleBookmark(schemeId: String) {
    prefsRepo.toggleBookmark(schemeId)
  }

  fun toggleDocumentCheck(schemeId: String, docIndex: Int) {
    prefsRepo.toggleDocumentCheck(schemeId, docIndex)
  }

  fun toggleStepCheck(schemeId: String, stepIndex: Int) {
    prefsRepo.toggleStepCheck(schemeId, stepIndex)
  }

  // AI Advisor
  fun openAdvisor() {
    _isAdvisorOpen.value = true
  }

  fun closeAdvisor() {
    _isAdvisorOpen.value = false
  }

  fun sendAdvisorMessage(text: String) {
    if (text.isBlank()) return
    val userMsg = ChatMessage(senderIsUser = true, text = text.trim())
    val updated = _chatMessages.value + userMsg
    _chatMessages.value = updated
    _isAdvisorGenerating.value = true

    viewModelScope.launch {
      val result = GeminiClient.askSchemeAdvisor(updated, text.trim())
      _isAdvisorGenerating.value = false
      val responseMsg = ChatMessage(
        senderIsUser = false,
        text = result.getOrElse { err ->
          "We could not reach the advisor at this moment (${err.localizedMessage}). You can always check official scheme documentation at https://www.myscheme.gov.in/."
        }
      )
      _chatMessages.value = _chatMessages.value + responseMsg
    }
  }

  // Google Sign-In sync affordance
  fun connectGoogleAccount(email: String) {
    _isGoogleConnected.value = true
    _userAccountEmail.value = email
  }

  fun disconnectGoogleAccount() {
    _isGoogleConnected.value = false
    _userAccountEmail.value = null
  }
}
