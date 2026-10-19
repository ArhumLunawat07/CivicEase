package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.CivicEaseBottomNav
import com.example.ui.components.CivicEaseTopAppBar
import com.example.ui.screens.*
import com.example.ui.theme.CivicEaseTheme
import com.example.ui.viewmodel.AppNavScreen
import com.example.ui.viewmodel.CivicEaseViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      CivicEaseTheme {
        CivicEaseApp()
      }
    }
  }
}

@Composable
fun CivicEaseApp(viewModel: CivicEaseViewModel = viewModel()) {
  val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
  val questionnaireState by viewModel.questionnaireState.collectAsStateWithLifecycle()
  val filterState by viewModel.filterState.collectAsStateWithLifecycle()
  val matchResults by viewModel.matchResults.collectAsStateWithLifecycle()
  val savedStateMap by viewModel.savedSchemesState.collectAsStateWithLifecycle()
  val selectedDetailScheme by viewModel.selectedSchemeForDetail.collectAsStateWithLifecycle()
  val isAdvisorOpen by viewModel.isAdvisorOpen.collectAsStateWithLifecycle()
  val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
  val isAdvisorGenerating by viewModel.isAdvisorGenerating.collectAsStateWithLifecycle()
  val isGoogleConnected by viewModel.isGoogleConnected.collectAsStateWithLifecycle()
  val userEmail by viewModel.userAccountEmail.collectAsStateWithLifecycle()

  var showGoogleSyncDialog by remember { mutableStateOf(false) }

  // Graceful BackHandler handling per Jetpack Compose best practices
  BackHandler(enabled = isAdvisorOpen || selectedDetailScheme != null || currentScreen != AppNavScreen.LANDING) {
    when {
      isAdvisorOpen -> viewModel.closeAdvisor()
      selectedDetailScheme != null -> viewModel.closeSchemeDetail()
      currentScreen == AppNavScreen.FINDER -> {
        if (questionnaireState.currentStep > 1) {
          viewModel.previousQuestionnaireStep()
        } else {
          viewModel.navigateTo(AppNavScreen.LANDING)
        }
      }
      currentScreen != AppNavScreen.LANDING -> viewModel.navigateTo(AppNavScreen.LANDING)
    }
  }

  Scaffold(
    topBar = {
      CivicEaseTopAppBar(
        savedCount = savedStateMap.size,
        onOpenAdvisor = { viewModel.openAdvisor() },
        onOpenSaved = { viewModel.navigateTo(AppNavScreen.SAVED) }
      )
    },
    bottomBar = {
      CivicEaseBottomNav(
        currentScreen = currentScreen,
        savedCount = savedStateMap.size,
        onNavigate = { screen -> viewModel.navigateTo(screen) }
      )
    },
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
        when (screen) {
          AppNavScreen.LANDING -> {
            LandingScreen(
              onStartFinder = { viewModel.navigateTo(AppNavScreen.FINDER) },
              onExploreAllSchemes = { viewModel.navigateTo(AppNavScreen.RESULTS) },
              onSelectCategory = { cat ->
                viewModel.selectCategoryFilter(cat)
                viewModel.navigateTo(AppNavScreen.RESULTS)
              },
              onOpenAdvisor = { viewModel.openAdvisor() }
            )
          }

          AppNavScreen.FINDER -> {
            FinderScreen(
              state = questionnaireState,
              onUpdateStateOrUT = { viewModel.updateStateOrUT(it) },
              onUpdateAgeGroup = { viewModel.updateAgeGroup(it) },
              onUpdatePrimarySector = { viewModel.updatePrimarySector(it) },
              onUpdateOccupation = { viewModel.updateOccupation(it) },
              onUpdateIncomeBracket = { viewModel.updateIncomeBracket(it) },
              onToggleSpecialCategory = { viewModel.toggleSpecialCategory(it) },
              onNextStep = { viewModel.nextQuestionnaireStep() },
              onPreviousStep = { viewModel.previousQuestionnaireStep() },
              onJumpToStep = { viewModel.jumpToStep(it) },
              onReset = { viewModel.resetQuestionnaire() }
            )
          }

          AppNavScreen.RESULTS -> {
            ResultsScreen(
              answers = questionnaireState.answers,
              matchResults = matchResults,
              filterState = filterState,
              savedStateMap = savedStateMap,
              onUpdateSearchQuery = { viewModel.updateSearchQuery(it) },
              onSelectCategoryFilter = { viewModel.selectCategoryFilter(it) },
              onSelectLevelFilter = { viewModel.selectLevelFilter(it) },
              onUpdateSortOption = { viewModel.updateSortOption(it) },
              onOpenSchemeDetail = { scheme -> viewModel.openSchemeDetail(scheme) },
              onToggleBookmark = { id -> viewModel.toggleBookmark(id) },
              onChangeAnswers = { viewModel.navigateTo(AppNavScreen.FINDER) }
            )
          }

          AppNavScreen.SAVED -> {
            SavedSchemesScreen(
              savedMap = savedStateMap,
              isGoogleConnected = isGoogleConnected,
              userEmail = userEmail,
              onConnectGoogle = { showGoogleSyncDialog = true },
              onDisconnectGoogle = { viewModel.disconnectGoogleAccount() },
              onOpenSchemeDetail = { scheme -> viewModel.openSchemeDetail(scheme) },
              onToggleBookmark = { id -> viewModel.toggleBookmark(id) },
              onToggleDocumentCheck = { id, docIdx -> viewModel.toggleDocumentCheck(id, docIdx) },
              onExploreSchemes = { viewModel.navigateTo(AppNavScreen.RESULTS) }
            )
          }
        }
      }
    }
  }

  // Scheme Detail Bottom Sheet
  selectedDetailScheme?.let { scheme ->
    SchemeDetailSheet(
      scheme = scheme,
      savedState = savedStateMap[scheme.id],
      onToggleBookmark = { viewModel.toggleBookmark(scheme.id) },
      onToggleDocumentCheck = { docIdx -> viewModel.toggleDocumentCheck(scheme.id, docIdx) },
      onToggleStepCheck = { stepIdx -> viewModel.toggleStepCheck(scheme.id, stepIdx) },
      onOpenAdvisorWithScheme = { initialQuestion ->
        viewModel.openAdvisor()
        viewModel.sendAdvisorMessage(initialQuestion)
      },
      onDismiss = { viewModel.closeSchemeDetail() }
    )
  }

  // AI Scheme Advisor Chat Sheet
  if (isAdvisorOpen) {
    SchemeAdvisorChatSheet(
      messages = chatMessages,
      isGenerating = isAdvisorGenerating,
      onSendMessage = { text -> viewModel.sendAdvisorMessage(text) },
      onDismiss = { viewModel.closeAdvisor() }
    )
  }

  // Google Account Sync Dialog
  if (showGoogleSyncDialog) {
    AlertDialog(
      onDismissRequest = { showGoogleSyncDialog = false },
      title = { Text("Sync with Google Account", fontWeight = FontWeight.Bold) },
      text = {
        Text(
          "Connect your Google account to back up your saved schemes, application checklists, and questionnaire answers to the cloud. You can disconnect at any time.",
          fontSize = 13.sp
        )
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.connectGoogleAccount("citizen.user@gmail.com")
            showGoogleSyncDialog = false
          }
        ) {
          Text("Connect Google")
        }
      },
      dismissButton = {
        TextButton(onClick = { showGoogleSyncDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
