package com.example.data.model

enum class SchemeCategory(val displayName: String, val description: String) {
  EDUCATION("Education", "Scholarships, fellowships, student loans & skill development"),
  HEALTHCARE("Healthcare", "Medical cover, maternity benefits & clinical treatments"),
  AGRICULTURE("Agriculture", "Crop support, farmer subsidies & farm equipment assistance"),
  SOCIAL_SUPPORT("Social Support", "Pensions, disability allowances & welfare security"),
  HOUSING("Housing", "Subsidized home construction, urban & rural shelter"),
  EMPLOYMENT("Employment", "Apprenticeships, self-employment capital & job skilling")
}

enum class SchemeLevel(val displayName: String) {
  CENTRAL("Central Government"),
  STATE("State Government")
}

enum class MatchStatus(val label: String, val subtitle: String) {
  POTENTIAL_MATCH("Potential match", "All provided criteria align with official requirements"),
  ONE_REQUIREMENT_UNCONFIRMED("Check one more requirement", "Most details match, verify income or state guidelines"),
  MORE_INFO_NEEDED("More information needed", "General scheme matching your interest; verify individual eligibility")
}

enum class AgeGroup(val label: String) {
  BELOW_18("Under 18 years"),
  AGE_18_TO_25("18 – 25 years (Youth/Student)"),
  AGE_26_TO_40("26 – 40 years (Working Age)"),
  AGE_41_TO_60("41 – 60 years (Mature Adult)"),
  ABOVE_60("60+ years (Senior Citizen)")
}

enum class OccupationStatus(val label: String) {
  STUDENT("Student / Scholar"),
  SMALL_FARMER("Small / Marginal Farmer"),
  DAILY_WAGE("Daily Wage / Informal Worker"),
  SELF_EMPLOYED("Small Business / Self-Employed"),
  SALARIED("Salaried Employee"),
  UNEMPLOYED("Currently Seeking Work"),
  HOMEMAKER("Homemaker / Unpaid Caregiver")
}

enum class IncomeBracket(val label: String, val maxLakhsPerYear: Float?) {
  BELOW_1_LAKH("Below ₹1 Lakh / year", 1.0f),
  BETWEEN_1_AND_2_5("₹1 Lakh – ₹2.5 Lakhs / year", 2.5f),
  BETWEEN_2_5_AND_5("₹2.5 Lakhs – ₹5 Lakhs / year", 5.0f),
  ABOVE_5_LAKHS("Above ₹5 Lakhs / year", null),
  PREFER_NOT_TO_SAY("Prefer not to say", null)
}

enum class SpecialCategory(val label: String) {
  BPL_RATION_CARD("BPL / Antyodaya Ration Card Holder"),
  PERSON_WITH_DISABILITY("Person with Benchmark Disability (PwD)"),
  SC_ST_COMMUNITY("Scheduled Caste / Scheduled Tribe"),
  WOMAN_APPLICANT("Women Applicant / Single Mother"),
  MINORITY_COMMUNITY("Religious / Linguistic Minority"),
  RURAL_RESIDENT("Rural / Gram Panchayat Resident")
}

data class EligibilityCriterion(
  val title: String,
  val isMandatory: Boolean,
  val explanation: String
)

data class Scheme(
  val id: String,
  val name: String,
  val shortCode: String,
  val category: SchemeCategory,
  val level: SchemeLevel,
  val applicableStates: List<String> = emptyList(), // Empty indicates Pan-India
  val department: String,
  val briefSummary: String,
  val targetBeneficiary: String,
  val eligibilityCriteria: List<EligibilityCriterion>,
  val benefitsOverview: String,
  val requiredDocuments: List<String>,
  val applicationSteps: List<String>,
  val officialUrl: String,
  val verifiedStatus: Boolean,
  val lastVerifiedDate: String,
  val allowedAgeGroups: List<AgeGroup> = emptyList(),
  val allowedOccupations: List<OccupationStatus> = emptyList(),
  val maxIncomeLevelLakhs: Float? = null,
  val beneficialCategories: List<SpecialCategory> = emptyList()
)

data class UserProfileAnswers(
  val stateOrUT: String = "",
  val ageGroup: AgeGroup? = null,
  val primarySector: SchemeCategory? = null,
  val occupationStatus: OccupationStatus? = null,
  val incomeBracket: IncomeBracket? = null,
  val specialCategories: Set<SpecialCategory> = emptySet()
)

data class SchemeMatchResult(
  val scheme: Scheme,
  val matchStatus: MatchStatus,
  val matchedReasons: List<String>,
  val pendingVerification: List<String>,
  val rankingScore: Int
)

data class SavedSchemeState(
  val schemeId: String,
  val savedAtMillis: Long = System.currentTimeMillis(),
  val completedDocIndices: Set<Int> = emptySet(),
  val completedStepIndices: Set<Int> = emptySet()
)
