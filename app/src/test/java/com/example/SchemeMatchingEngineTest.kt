package com.example

import com.example.data.model.*
import com.example.data.repository.SchemeRepository
import org.junit.Assert.*
import org.junit.Test

class SchemeMatchingEngineTest {

  @Test
  fun testStudentMatching() {
    val answers = UserProfileAnswers(
      stateOrUT = "Maharashtra",
      ageGroup = AgeGroup.AGE_18_TO_25,
      primarySector = SchemeCategory.EDUCATION,
      occupationStatus = OccupationStatus.STUDENT,
      incomeBracket = IncomeBracket.BETWEEN_1_AND_2_5
    )

    val results = SchemeRepository.evaluateMatches(answers)
    assertTrue("Should return matched schemes", results.isNotEmpty())

    // Post-Matric Scholarship should rank near the top for a student with matching income
    val postMatric = results.find { it.scheme.id == "post-matric-scholarship" }
    assertNotNull("Post-Matric Scholarship should be in the results", postMatric)
    assertEquals(MatchStatus.POTENTIAL_MATCH, postMatric!!.matchStatus)
    assertTrue(postMatric.matchedReasons.any { it.contains("Education") })
    assertTrue(postMatric.matchedReasons.any { it.contains("Student") })
  }

  @Test
  fun testFarmerMatching() {
    val answers = UserProfileAnswers(
      stateOrUT = "Punjab",
      ageGroup = AgeGroup.AGE_26_TO_40,
      primarySector = SchemeCategory.AGRICULTURE,
      occupationStatus = OccupationStatus.SMALL_FARMER,
      incomeBracket = IncomeBracket.BETWEEN_1_AND_2_5
    )

    val results = SchemeRepository.evaluateMatches(answers)
    val pmKisan = results.find { it.scheme.id == "pm-kisan" }
    assertNotNull("PM-Kisan should be found", pmKisan)
    assertEquals(MatchStatus.POTENTIAL_MATCH, pmKisan!!.matchStatus)
  }

  @Test
  fun testHealthcareCover() {
    val answers = UserProfileAnswers(
      stateOrUT = "Rajasthan",
      ageGroup = AgeGroup.AGE_41_TO_60,
      primarySector = SchemeCategory.HEALTHCARE,
      occupationStatus = OccupationStatus.DAILY_WAGE,
      incomeBracket = IncomeBracket.BELOW_1_LAKH,
      specialCategories = setOf(SpecialCategory.BPL_RATION_CARD)
    )

    val results = SchemeRepository.evaluateMatches(answers)
    val pmjay = results.find { it.scheme.id == "pm-jay" }
    assertNotNull("PM-JAY should be matched for BPL daily wage worker", pmjay)
    assertEquals(MatchStatus.POTENTIAL_MATCH, pmjay!!.matchStatus)
  }
}
