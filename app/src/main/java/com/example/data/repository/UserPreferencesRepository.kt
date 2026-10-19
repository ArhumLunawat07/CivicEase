package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.SavedSchemeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferencesRepository(context: Context) {

  private val prefs: SharedPreferences =
    context.applicationContext.getSharedPreferences("civicease_prefs", Context.MODE_PRIVATE)

  private val _savedSchemesState = MutableStateFlow<Map<String, SavedSchemeState>>(loadSavedState())
  val savedSchemesState: StateFlow<Map<String, SavedSchemeState>> = _savedSchemesState.asStateFlow()

  private fun loadSavedState(): Map<String, SavedSchemeState> {
    val result = mutableMapOf<String, SavedSchemeState>()
    val savedIds = prefs.getStringSet("saved_scheme_ids", emptySet()) ?: emptySet()
    for (id in savedIds) {
      val docsKey = "docs_$id"
      val stepsKey = "steps_$id"
      val docIndices = prefs.getStringSet(docsKey, emptySet())?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
      val stepIndices = prefs.getStringSet(stepsKey, emptySet())?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
      result[id] = SavedSchemeState(
        schemeId = id,
        completedDocIndices = docIndices,
        completedStepIndices = stepIndices
      )
    }
    return result
  }

  fun toggleBookmark(schemeId: String) {
    val current = _savedSchemesState.value.toMutableMap()
    val savedIds = prefs.getStringSet("saved_scheme_ids", emptySet())?.toMutableSet() ?: mutableSetOf()

    if (current.containsKey(schemeId)) {
      current.remove(schemeId)
      savedIds.remove(schemeId)
      prefs.edit()
        .putStringSet("saved_scheme_ids", savedIds)
        .remove("docs_$schemeId")
        .remove("steps_$schemeId")
        .apply()
    } else {
      val newState = SavedSchemeState(schemeId = schemeId)
      current[schemeId] = newState
      savedIds.add(schemeId)
      prefs.edit().putStringSet("saved_scheme_ids", savedIds).apply()
    }
    _savedSchemesState.value = current
  }

  fun toggleDocumentCheck(schemeId: String, docIndex: Int) {
    val current = _savedSchemesState.value.toMutableMap()
    val existing = current[schemeId] ?: SavedSchemeState(schemeId = schemeId)
    val updatedDocs = existing.completedDocIndices.toMutableSet()

    if (updatedDocs.contains(docIndex)) {
      updatedDocs.remove(docIndex)
    } else {
      updatedDocs.add(docIndex)
    }

    val updatedState = existing.copy(completedDocIndices = updatedDocs)
    current[schemeId] = updatedState
    _savedSchemesState.value = current

    // Persist
    val savedIds = prefs.getStringSet("saved_scheme_ids", emptySet())?.toMutableSet() ?: mutableSetOf()
    savedIds.add(schemeId)
    prefs.edit()
      .putStringSet("saved_scheme_ids", savedIds)
      .putStringSet("docs_$schemeId", updatedDocs.map { it.toString() }.toSet())
      .apply()
  }

  fun toggleStepCheck(schemeId: String, stepIndex: Int) {
    val current = _savedSchemesState.value.toMutableMap()
    val existing = current[schemeId] ?: SavedSchemeState(schemeId = schemeId)
    val updatedSteps = existing.completedStepIndices.toMutableSet()

    if (updatedSteps.contains(stepIndex)) {
      updatedSteps.remove(stepIndex)
    } else {
      updatedSteps.add(stepIndex)
    }

    val updatedState = existing.copy(completedStepIndices = updatedSteps)
    current[schemeId] = updatedState
    _savedSchemesState.value = current

    // Persist
    val savedIds = prefs.getStringSet("saved_scheme_ids", emptySet())?.toMutableSet() ?: mutableSetOf()
    savedIds.add(schemeId)
    prefs.edit()
      .putStringSet("saved_scheme_ids", savedIds)
      .putStringSet("steps_$schemeId", updatedSteps.map { it.toString() }.toSet())
      .apply()
  }

  fun isBookmarked(schemeId: String): Boolean {
    return _savedSchemesState.value.containsKey(schemeId)
  }

  fun getSchemeState(schemeId: String): SavedSchemeState? {
    return _savedSchemesState.value[schemeId]
  }
}
