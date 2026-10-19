package com.example.data.repository

import com.example.data.model.*

object SchemeRepository {

  val INDIAN_STATES_AND_UTS = listOf(
    "All India (Central)",
    "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
    "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka",
    "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram",
    "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
    "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal",
    "Andaman and Nicobar Islands", "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu",
    "Delhi (NCT)", "Jammu and Kashmir", "Ladakh", "Lakshadweep", "Puducherry"
  )

  val SCHEMES: List<Scheme> = listOf(
    Scheme(
      id = "pm-jay",
      name = "Ayushman Bharat – Pradhan Mantri Jan Arogya Yojana (PM-JAY)",
      shortCode = "PM-JAY",
      category = SchemeCategory.HEALTHCARE,
      level = SchemeLevel.CENTRAL,
      applicableStates = emptyList(), // Pan-India
      department = "National Health Authority, Ministry of Health and Family Welfare",
      briefSummary = "Health cover of up to ₹5 Lakh per family per year for secondary and tertiary care hospitalization across empanelled public and private hospitals.",
      targetBeneficiary = "Deprived rural families and identified occupational categories of urban workers' families based on SECC 2011 data.",
      eligibilityCriteria = listOf(
        EligibilityCriterion(
          title = "Socio-Economic Census / Ration Registry",
          isMandatory = true,
          explanation = "Identified as eligible under the Socio-Economic and Caste Census (SECC 2011) or state-linked NFSA ration card lists."
        ),
        EligibilityCriterion(
          title = "Family Size & Age Limit",
          isMandatory = false,
          explanation = "No restrictions on family size, age, or gender; pre-existing conditions are covered from day one."
        )
      ),
      benefitsOverview = "Cashless and paperless access to healthcare services at the point of service in any empanelled hospital across India.",
      requiredDocuments = listOf(
        "Aadhaar Card or Government Photo Identity Card",
        "Ration Card or PM-JAY Family ID letter",
        "Registered Mobile Number for OTP verification"
      ),
      applicationSteps = listOf(
        "Check family eligibility online via the official PM-JAY portal or nearest Ayushman Mitra kiosk at an empanelled hospital.",
        "Present your Aadhaar card and Ration Card to the Ayushman Mitra or Common Service Centre (CSC).",
        "Complete biometric e-KYC authentication.",
        "Receive your laminated Ayushman Card or digital Golden Card instantly."
      ),
      officialUrl = "https://www.myscheme.gov.in/schemes/pmjay",
      verifiedStatus = true,
      lastVerifiedDate = "January 2026",
      allowedAgeGroups = AgeGroup.values().toList(),
      allowedOccupations = listOf(
        OccupationStatus.DAILY_WAGE,
        OccupationStatus.SMALL_FARMER,
        OccupationStatus.HOMEMAKER,
        OccupationStatus.UNEMPLOYED,
        OccupationStatus.SELF_EMPLOYED
      ),
      maxIncomeLevelLakhs = 2.5f,
      beneficialCategories = listOf(
        SpecialCategory.BPL_RATION_CARD,
        SpecialCategory.RURAL_RESIDENT,
        SpecialCategory.SC_ST_COMMUNITY
      )
    ),

    Scheme(
      id = "pm-kisan",
      name = "Pradhan Mantri Kisan Samman Nidhi (PM-KISAN)",
      shortCode = "PM-KISAN",
      category = SchemeCategory.AGRICULTURE,
      level = SchemeLevel.CENTRAL,
      applicableStates = emptyList(),
      department = "Department of Agriculture & Farmers Welfare, Ministry of Agriculture",
      briefSummary = "Direct income support of ₹6,000 per year transferred in three equal four-monthly installments directly into bank accounts of landholding farmer families.",
      targetBeneficiary = "Small and marginal landholding farmer families who own cultivable land.",
      eligibilityCriteria = listOf(
        EligibilityCriterion(
          title = "Cultivable Land Ownership",
          isMandatory = true,
          explanation = "The farmer family must hold valid land records in their name in the state revenue records."
        ),
        EligibilityCriterion(
          title = "Institutional Landholder Exclusion",
          isMandatory = true,
          explanation = "Institutional landholders, constitutional post-holders, and income tax payees are excluded."
        )
      ),
      benefitsOverview = "₹6,000 per year via Direct Benefit Transfer (DBT) to support input costs for crops and domestic needs.",
      requiredDocuments = listOf(
        "Aadhaar Card (Aadhaar-seeded bank account mandatory)",
        "Landholding Documentation (RoR / Khasra / Khatauni)",
        "Active Bank Account details (IFSC code, DBT-enabled)",
        "Mobile Number linked with Aadhaar"
      ),
      applicationSteps = listOf(
        "Visit the official Farmers Corner on PM-KISAN portal or visit a local Common Service Centre (CSC).",
        "Click on 'New Farmer Registration' and enter your Aadhaar number and state.",
        "Fill in land records details (survey number, dag/khasra number, land size).",
        "Submit the application for verification by the State Nodal Officer and complete mandatory OTP e-KYC."
      ),
      officialUrl = "https://www.myscheme.gov.in/schemes/pm-kisan",
      verifiedStatus = true,
      lastVerifiedDate = "March 2026",
      allowedAgeGroups = listOf(
        AgeGroup.AGE_18_TO_25,
        AgeGroup.AGE_26_TO_40,
        AgeGroup.AGE_41_TO_60,
        AgeGroup.ABOVE_60
      ),
      allowedOccupations = listOf(OccupationStatus.SMALL_FARMER),
      maxIncomeLevelLakhs = null,
      beneficialCategories = listOf(SpecialCategory.RURAL_RESIDENT)
    ),

    Scheme(
      id = "post-matric-scholarship",
      name = "Post-Matric Scholarship Scheme for Higher Education",
      shortCode = "PMS-HE",
      category = SchemeCategory.EDUCATION,
      level = SchemeLevel.CENTRAL,
      applicableStates = emptyList(),
      department = "Ministry of Social Justice and Empowerment / Ministry of Tribal Affairs",
      briefSummary = "Financial assistance covering mandatory tuition fees and monthly maintenance allowance for students pursuing post-secondary recognized courses.",
      targetBeneficiary = "Students enrolled in recognized post-matriculation or post-secondary courses in government or accredited private institutes.",
      eligibilityCriteria = listOf(
        EligibilityCriterion(
          title = "Annual Household Income Ceiling",
          isMandatory = true,
          explanation = "Total family income from all sources must not exceed ₹2.50 Lakhs per annum."
        ),
        EligibilityCriterion(
          title = "Institutional Enrollment",
          isMandatory = true,
          explanation = "Must be studying in Class 11, 12, ITI, Diploma, Undergraduate, or Postgraduate programs."
        )
      ),
      benefitsOverview = "Reimbursement of non-refundable compulsory course fees directly to institution or student, plus regular maintenance allowance.",
      requiredDocuments = listOf(
        "Previous Year Academic Marksheet / Passing Certificate",
        "Recent Family Income Certificate issued by authorized revenue authority",
        "Community / Caste Certificate if applicable",
        "Bonafide Student Certificate / College Admission Fee Receipt",
        "Aadhaar-seeded Bank Account Passbook"
      ),
      applicationSteps = listOf(
        "Register on the National Scholarship Portal (NSP) with valid OTR (One-Time Registration).",
        "Fill the Post-Matric application form selecting your verified institution and course details.",
        "Upload scanned self-attested copies of required income and academic documents.",
        "Submit form online and notify your college nodal verification officer for institute-level e-verification."
      ),
      officialUrl = "https://www.myscheme.gov.in/schemes/pmssc",
      verifiedStatus = true,
      lastVerifiedDate = "February 2026",
      allowedAgeGroups = listOf(AgeGroup.BELOW_18, AgeGroup.AGE_18_TO_25),
      allowedOccupations = listOf(OccupationStatus.STUDENT),
      maxIncomeLevelLakhs = 2.5f,
      beneficialCategories = listOf(
        SpecialCategory.SC_ST_COMMUNITY,
        SpecialCategory.PERSON_WITH_DISABILITY,
        SpecialCategory.MINORITY_COMMUNITY
      )
    ),

    Scheme(
      id = "pmay-urban-rural",
      name = "Pradhan Mantri Awas Yojana (PMAY – Housing for All)",
      shortCode = "PMAY",
      category = SchemeCategory.HOUSING,
      level = SchemeLevel.CENTRAL,
      applicableStates = emptyList(),
      department = "Ministry of Housing and Urban Affairs / Ministry of Rural Development",
      briefSummary = "Interest subsidies on housing loans (CLSS) and financial grants for constructing or upgrading pucca houses equipped with basic civic amenities.",
      targetBeneficiary = "Families without a permanent (pucca) house anywhere in India, categorized under EWS, LIG, or rural housing registries.",
      eligibilityCriteria = listOf(
        EligibilityCriterion(
          title = "No Pucca House Ownership",
          isMandatory = true,
          explanation = "The beneficiary family must not own a pucca house in their name or family members' names anywhere in India."
        ),
        EligibilityCriterion(
          title = "Income Group Classification",
          isMandatory = true,
          explanation = "Economically Weaker Section (EWS) up to ₹3 Lakhs/year; Lower Income Group (LIG) up to ₹6 Lakhs/year."
        )
      ),
      benefitsOverview = "Central financial assistance of up to ₹1.2 Lakh (rural plain) to ₹2.67 Lakh (urban interest subsidy) for house construction.",
      requiredDocuments = listOf(
        "Aadhaar cards of all family members",
        "Income Certificate / Salary Slip / Affidavit of Income",
        "Land Ownership Deed or Gram Panchayat building permission",
        "Bank Account details (DBT enabled)",
        "Affidavit confirming no existing pucca house in India"
      ),
      applicationSteps = listOf(
        "Urban applicants: Apply online through PMAY citizen assessment portal or through registered CSC kiosks.",
        "Rural applicants: Selected through Gram Sabha-approved Awaas+ prioritization list.",
        "Submit land coordinates and geo-tagged photographs of existing kucha shelter.",
        "Tracking approval via Application Reference ID on the official PMAY portal."
      ),
      officialUrl = "https://www.myscheme.gov.in/schemes/pmay-g",
      verifiedStatus = true,
      lastVerifiedDate = "January 2026",
      allowedAgeGroups = listOf(
        AgeGroup.AGE_18_TO_25,
        AgeGroup.AGE_26_TO_40,
        AgeGroup.AGE_41_TO_60,
        AgeGroup.ABOVE_60
      ),
      allowedOccupations = OccupationStatus.values().toList(),
      maxIncomeLevelLakhs = 5.0f,
      beneficialCategories = listOf(
        SpecialCategory.BPL_RATION_CARD,
        SpecialCategory.RURAL_RESIDENT,
        SpecialCategory.WOMAN_APPLICANT
      )
    ),

    Scheme(
      id = "atal-pension-yojana",
      name = "Atal Pension Yojana (APY)",
      shortCode = "APY",
      category = SchemeCategory.SOCIAL_SUPPORT,
      level = SchemeLevel.CENTRAL,
      applicableStates = emptyList(),
      department = "Pension Fund Regulatory and Development Authority (PFRDA), Ministry of Finance",
      briefSummary = "Guaranteed monthly pension of ₹1,000 to ₹5,000 upon reaching 60 years of age, based on voluntary monthly contributions.",
      targetBeneficiary = "All Indian citizens between 18 to 40 years of age holding a savings bank account, focused on unorganized sector workers.",
      eligibilityCriteria = listOf(
        EligibilityCriterion(
          title = "Age Window",
          isMandatory = true,
          explanation = "Must be between 18 and 40 years at the time of joining."
        ),
        EligibilityCriterion(
          title = "Tax Payer Exclusion",
          isMandatory = true,
          explanation = "Citizen must not be an income taxpayer as per applicable rules."
        )
      ),
      benefitsOverview = "Fixed minimum monthly pension for life to subscriber; thereafter to spouse; return of pension corpus to subscriber's nominee.",
      requiredDocuments = listOf(
        "Aadhaar Card",
        "Savings Bank Account / Post Office Account details with auto-debit facility",
        "Active Mobile Number",
        "Nominee identification details"
      ),
      applicationSteps = listOf(
        "Approach the bank or post office branch where your savings account is held.",
        "Fill out the APY Registration Form and opt for your desired monthly pension amount (₹1,000 to ₹5,000).",
        "Provide Aadhaar and nominee details.",
        "Ensure sufficient monthly balance in your savings account for automated debit deduction."
      ),
      officialUrl = "https://www.myscheme.gov.in/schemes/apy",
      verifiedStatus = true,
      lastVerifiedDate = "March 2026",
      allowedAgeGroups = listOf(
        AgeGroup.AGE_18_TO_25,
        AgeGroup.AGE_26_TO_40
      ),
      allowedOccupations = listOf(
        OccupationStatus.DAILY_WAGE,
        OccupationStatus.SMALL_FARMER,
        OccupationStatus.SELF_EMPLOYED,
        OccupationStatus.HOMEMAKER,
        OccupationStatus.UNEMPLOYED
      ),
      maxIncomeLevelLakhs = 5.0f,
      beneficialCategories = emptyList()
    ),

    Scheme(
      id = "naps-apprenticeship",
      name = "National Apprenticeship Promotion Scheme (NAPS)",
      shortCode = "NAPS",
      category = SchemeCategory.EMPLOYMENT,
      level = SchemeLevel.CENTRAL,
      applicableStates = emptyList(),
      department = "Ministry of Skill Development and Entrepreneurship (MSDE)",
      briefSummary = "Promotes industry apprenticeship by offering stipend support directly to youths undergoing practical shop-floor industrial training.",
      targetBeneficiary = "Job-seekers and vocational students who have completed Class 5 or above, ITI, Diploma, or Graduate degrees.",
      eligibilityCriteria = listOf(
        EligibilityCriterion(
          title = "Minimum Age",
          isMandatory = true,
          explanation = "Candidate must have attained a minimum age of 14 years (18 years for hazardous occupations)."
        ),
        EligibilityCriterion(
          title = "Educational Qualification",
          isMandatory = false,
          explanation = "Minimum educational qualifications vary per designated trade (ranging from Class 8 to engineering degree)."
        )
      ),
      benefitsOverview = "Government reimburses 25% of prescribed stipend up to ₹1,500 per month directly to apprentices via DBT.",
      requiredDocuments = listOf(
        "Educational certificates / ITI National Trade Certificate",
        "Aadhaar Card for identity verification",
        "Passport size photograph",
        "Active Bank Account for stipend DBT credit"
      ),
      applicationSteps = listOf(
        "Register as an Apprentice Candidate on the official Apprenticeship India Portal.",
        "Complete e-KYC using Aadhaar and fill out profile education details.",
        "Browse open apprenticeship vacancies by sector, location, or trade stipend.",
        "Apply to desired establishment and sign the apprenticeship contract online upon selection."
      ),
      officialUrl = "https://www.myscheme.gov.in/schemes/naps",
      verifiedStatus = true,
      lastVerifiedDate = "February 2026",
      allowedAgeGroups = listOf(AgeGroup.BELOW_18, AgeGroup.AGE_18_TO_25, AgeGroup.AGE_26_TO_40),
      allowedOccupations = listOf(
        OccupationStatus.STUDENT,
        OccupationStatus.UNEMPLOYED,
        OccupationStatus.DAILY_WAGE
      ),
      maxIncomeLevelLakhs = null,
      beneficialCategories = emptyList()
    ),

    Scheme(
      id = "pm-mudra-yojana",
      name = "Pradhan Mantri MUDRA Yojana (PMMY)",
      shortCode = "PMMY",
      category = SchemeCategory.EMPLOYMENT,
      level = SchemeLevel.CENTRAL,
      applicableStates = emptyList(),
      department = "Department of Financial Services, Ministry of Finance",
      briefSummary = "Collateral-free micro loans up to ₹10 Lakhs (Shishu, Kishore, and Tarun) for non-corporate, non-farm small and micro enterprises.",
      targetBeneficiary = "Small shopkeepers, artisans, street vendors, small manufacturers, and women entrepreneurs seeking business capital.",
      eligibilityCriteria = listOf(
        EligibilityCriterion(
          title = "Non-Farm Business Purpose",
          isMandatory = true,
          explanation = "Loan must be utilized for income-generating micro activities in manufacturing, trading, or service sectors."
        ),
        EligibilityCriterion(
          title = "Credit History",
          isMandatory = true,
          explanation = "Applicant must not be a defaulter to any commercial bank, cooperative bank, or financial institution."
        )
      ),
      benefitsOverview = "No collateral or third-party guarantee required; competitive interest rates determined by lending bank guidelines.",
      requiredDocuments = listOf(
        "Proof of Identity (Aadhaar / Voter ID / Passport)",
        "Proof of Residence (Electricity bill / Ration card)",
        "Business Enterprise Proof (Udyam Registration / Shop & Establishment Certificate)",
        "Bank Statement for last 6 months",
        "Quotation of machinery / project proposal for proposed activity"
      ),
      applicationSteps = listOf(
        "Prepare a brief business activity plan or quotation for items to be purchased.",
        "Apply online via UdyamiMitra portal or visit any public/private commercial bank or NBFC branch.",
        "Submit filled MUDRA application form with identity, residence, and enterprise documents.",
        "Upon sanction, receive MUDRA Debit Card to draw working capital as needed."
      ),
      officialUrl = "https://www.myscheme.gov.in/schemes/pmmy",
      verifiedStatus = true,
      lastVerifiedDate = "January 2026",
      allowedAgeGroups = listOf(AgeGroup.AGE_18_TO_25, AgeGroup.AGE_26_TO_40, AgeGroup.AGE_41_TO_60),
      allowedOccupations = listOf(
        OccupationStatus.SELF_EMPLOYED,
        OccupationStatus.DAILY_WAGE,
        OccupationStatus.HOMEMAKER
      ),
      maxIncomeLevelLakhs = null,
      beneficialCategories = listOf(SpecialCategory.WOMAN_APPLICANT)
    ),

    Scheme(
      id = "ignwps-widow-pension",
      name = "Indira Gandhi National Widow Pension Scheme (IGNWPS)",
      shortCode = "IGNWPS",
      category = SchemeCategory.SOCIAL_SUPPORT,
      level = SchemeLevel.CENTRAL,
      applicableStates = emptyList(),
      department = "Ministry of Rural Development (National Social Assistance Programme)",
      briefSummary = "Monthly social security pension for widows living below the poverty line to ensure dignified subsistence.",
      targetBeneficiary = "Widows aged 40–79 years belonging to a household categorized below the official poverty line (BPL).",
      eligibilityCriteria = listOf(
        EligibilityCriterion(
          title = "BPL Household Verification",
          isMandatory = true,
          explanation = "Applicant's family must be listed in the official BPL census registry."
        ),
        EligibilityCriterion(
          title = "Age Condition",
          isMandatory = true,
          explanation = "Applicant must be a widow between 40 and 79 years of age."
        )
      ),
      benefitsOverview = "Monthly monetary pension credited directly into bank or post office account, often augmented with state government co-funding.",
      requiredDocuments = listOf(
        "Death Certificate of Spouse",
        "BPL Ration Card / Proof of BPL status",
        "Age proof (Aadhaar Card, Birth Certificate, or School certificate)",
        "Bank or Post Office Passbook copy"
      ),
      applicationSteps = listOf(
        "Obtain application form from local Gram Panchayat, Block Development Officer (BDO), or Municipal Corporation office.",
        "Attach copies of spouse's death certificate, BPL ration card, and bank passbook.",
        "Submit to the Social Welfare Inspector or Ward Councillor for field verification.",
        "Sanction letter issued by the District Social Welfare Officer and monthly DBT commences."
      ),
      officialUrl = "https://www.myscheme.gov.in/schemes/ignwps",
      verifiedStatus = true,
      lastVerifiedDate = "February 2026",
      allowedAgeGroups = listOf(AgeGroup.AGE_41_TO_60, AgeGroup.ABOVE_60),
      allowedOccupations = listOf(
        OccupationStatus.HOMEMAKER,
        OccupationStatus.DAILY_WAGE,
        OccupationStatus.UNEMPLOYED
      ),
      maxIncomeLevelLakhs = 1.0f,
      beneficialCategories = listOf(
        SpecialCategory.BPL_RATION_CARD,
        SpecialCategory.WOMAN_APPLICANT
      )
    )
  )

  /**
   * Deterministic matching engine. Evaluates user questionnaire answers against structured scheme conditions.
   * Produces transparent explanations of why a scheme matches or what conditions require manual verification.
   */
  fun evaluateMatches(answers: UserProfileAnswers): List<SchemeMatchResult> {
    return SCHEMES.map { scheme ->
      val matchedReasons = mutableListOf<String>()
      val pendingVerification = mutableListOf<String>()
      var score = 0

      // 1. Sector / Category Matching
      if (answers.primarySector != null) {
        if (scheme.category == answers.primarySector) {
          matchedReasons.add("Directly aligns with your focus on ${scheme.category.displayName}")
          score += 35
        } else {
          score += 5
        }
      } else {
        matchedReasons.add("Category: ${scheme.category.displayName}")
        score += 15
      }

      // 2. State / Geography Matching
      if (answers.stateOrUT.isNotBlank() && answers.stateOrUT != "All India (Central)") {
        if (scheme.applicableStates.isEmpty() || scheme.applicableStates.contains(answers.stateOrUT)) {
          matchedReasons.add("Available in ${answers.stateOrUT} (Pan-India Central Scheme)")
          score += 20
        } else {
          pendingVerification.add("Check whether your state (${answers.stateOrUT}) has active bilateral implementation")
        }
      } else {
        matchedReasons.add("Nationwide Central Government Scheme")
        score += 15
      }

      // 3. Occupation Matching
      if (answers.occupationStatus != null) {
        if (scheme.allowedOccupations.isEmpty() || scheme.allowedOccupations.contains(answers.occupationStatus)) {
          matchedReasons.add("Eligible for ${answers.occupationStatus.label}")
          score += 25
        } else {
          pendingVerification.add("Primary target is ${scheme.targetBeneficiary}; verify if secondary occupational rules apply")
        }
      }

      // 4. Age Group Matching
      if (answers.ageGroup != null) {
        if (scheme.allowedAgeGroups.isEmpty() || scheme.allowedAgeGroups.contains(answers.ageGroup)) {
          matchedReasons.add("Age falls within designated cohort (${answers.ageGroup.label})")
          score += 15
        } else {
          pendingVerification.add("Verify age eligibility limits specified by department regulations")
        }
      }

      // 5. Income Ceiling Matching
      if (answers.incomeBracket != null && answers.incomeBracket != IncomeBracket.PREFER_NOT_TO_SAY) {
        val userMaxIncome = answers.incomeBracket.maxLakhsPerYear
        if (scheme.maxIncomeLevelLakhs != null) {
          if (userMaxIncome != null && userMaxIncome <= scheme.maxIncomeLevelLakhs) {
            matchedReasons.add("Reported household income (${answers.incomeBracket.label}) meets limit (≤ ₹${scheme.maxIncomeLevelLakhs}L)")
            score += 20
          } else if (userMaxIncome != null && userMaxIncome > scheme.maxIncomeLevelLakhs) {
            pendingVerification.add("Income threshold requirement: Scheme typically capped at ₹${scheme.maxIncomeLevelLakhs} Lakhs/year")
          }
        } else {
          matchedReasons.add("No restrictive income ceiling specified in primary guidelines")
          score += 10
        }
      } else if (scheme.maxIncomeLevelLakhs != null) {
        pendingVerification.add("Income threshold unverified: Requires annual family income under ₹${scheme.maxIncomeLevelLakhs} Lakhs")
      }

      // 6. Special Demographic Categories
      if (answers.specialCategories.isNotEmpty()) {
        val matchedSpecials = answers.specialCategories.filter { scheme.beneficialCategories.contains(it) }
        if (matchedSpecials.isNotEmpty()) {
          matchedReasons.add("Priority criteria match: ${matchedSpecials.joinToString { it.label }}")
          score += 20
        }
      }

      // Determine transparent status label
      val matchStatus = when {
        pendingVerification.isEmpty() && score >= 50 -> MatchStatus.POTENTIAL_MATCH
        pendingVerification.size <= 1 && score >= 30 -> MatchStatus.ONE_REQUIREMENT_UNCONFIRMED
        else -> MatchStatus.MORE_INFO_NEEDED
      }

      SchemeMatchResult(
        scheme = scheme,
        matchStatus = matchStatus,
        matchedReasons = matchedReasons,
        pendingVerification = pendingVerification,
        rankingScore = score
      )
    }.sortedByDescending { it.rankingScore }
  }
}
