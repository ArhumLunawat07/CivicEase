# CivicEase — Premium Government Benefits Discovery Platform

CivicEase empowers citizens across India to effortlessly discover relevant public schemes and government benefits, understand eligibility requirements in plain language, track application checklists, and consult an intelligent AI Scheme Advisor.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> The following architectural and UX decisions have been aligned based on user clarification answers:

- **Confirmed Decision 1 (Discovery & AI Assistance)**: The platform will provide a **deterministic questionnaire matcher** paired with an **AI Scheme Advisor chat** (using Gemini) to answer nuanced questions, explain eligibility in everyday language, and reference official portals (myScheme.gov.in / india.gov.in).
- **Confirmed Decision 2 (Authentication & Persistence Flow)**: **Guest mode first**. Citizens can explore schemes, complete the questionnaire, view match breakdowns, and manage local bookmarks immediately without forced login. Citizens can optionally **Sign in with Google** via Jetpack Credential Manager to synchronize bookmarks and checklist progress to Firebase Firestore.
- **Confirmed Decision 3 (Featured Sector Focus)**: Comprehensive launch catalog spanning **Education, Healthcare, Agriculture, and Social Support**, alongside Housing and Employment, with verified official department sources and practical document checklists.
- **Confirmed Decision 4 (Transparency & Safety Rule)**: Zero invented benefits, numbers, or false promises. Matches are categorized transparently as *"Potential match"*, *"Check one more requirement"*, or *"More information needed"*, distinguishing verified data from general guidance.

---

### 1. Overview & Core Concept

- **What It Does**: CivicEase demystifies the complex landscape of public benefits in India. Through a guided 5-step questionnaire, citizens identify potentially relevant schemes, receive plain-language eligibility breakdowns, step-by-step application instructions, required document checklists, and direct links to official portals.
- **Target Audience / Persona**: Students seeking scholarships, farmers looking for agricultural assistance, low-to-middle income families seeking healthcare and housing aid, small entrepreneurs, and senior citizens or persons with disabilities seeking social support.
- **Key Value**: Replaces confusing government bureaucracy and scattered portal jargon with a calm, trustworthy, human-centered discovery engine that respects privacy, requires no sensitive identification (no Aadhaar/bank numbers), and guides the user toward actionable next steps.

---

### 2. User Experience & Visual Design

#### Key User Flows

```
[Landing Page]
  │
  ├──► [Explore Catalog & Categories] ──► [Scheme Details & Checklist]
  │
  ├──► [Guided Benefits Finder (5 Steps)]
  │      ├─ 1. Location (State/UT) & Age Group
  │      ├─ 2. Primary Need / Sector Focus
  │      ├─ 3. Occupation & Student Status
  │      ├─ 4. Household Income Range (with skip option)
  │      ├─ 5. Review & Confirm Profile
  │      └─► [Benefits Shortlist & Matches]
  │             ├─ Filter by Category, Central vs State, Relevance
  │             ├─ Plain-Language Match Breakdown
  │             └─► [Scheme Detail Sheet & Document Checklist]
  │
  ├──► [Saved Schemes & Checklist Tracker] (Local or Cloud-Synced)
  │
  └──► [AI Scheme Advisor Chat] (Gemini-powered conversational guide)
```

#### Visual Identity & Theme
- **Aesthetic Direction**: Premium civic-tech and thoughtful productivity. Clean, trustworthy, and modern — reminiscent of a polished fintech application rather than a cluttered administrative portal.
- **Color Palette**:
  - *Background*: Warm off-white (`#F8FAF8` / Surface `#FFFFFF`)
  - *Typography & Dark Accents*: Deep navy (`#0F1E2E`)
  - *Primary Accents*: Forest Green (`#1E5E3A` / Deep Jade `#144229`)
  - *Surface Tones*: Soft Mint (`#E8F5EE` / `#EDF8F1`)
  - *Highlights & Status*: Restrained Amber (`#D97706` / `#FEF3C7`) and Subtle Sky Blue
  - *Card Borders*: Subtle warm dividers (`#E2E8F0`)
- **Typography & Hierarchy**:
  - Display & Headings: Bold, human-friendly sans-serif with distinct weights.
  - Body: High-legibility sans-serif with comfortable line height and accessible contrast.
- **Component Styling & Layout**:
  - Material 3 cards with gentle `16.dp` corner rounding and soft `1.dp` borders.
  - Generous padding and whitespace (`16.dp` to `24.dp`), clear progress steppers, and pill-shaped filter chips.
  - Explicit touch targets of $\ge 48\text{dp}$ with ripple feedback on all interactable elements.
- **Interactive Feedback & Motion**:
  - Smooth animated transitions between finder steps (`AnimatedContent`).
  - Interactive document checklist toggles with persistent state.
  - Bookmark feedback with toast/snackbar confirmation and optional Google sync prompt.
  - Full support for Android edge-to-edge drawing, respecting status bar and navigation bar insets.

---

### 3. Key Product Decisions & Trade-Offs

#### Decision 1: Deterministic Transparent Matching Engine
- **Chosen Approach**: Built-in deterministic rules engine that compares citizen answers (State, Age, Sector, Occupation, Income bracket, Special criteria) against structured scheme conditions.
- **Why**: Transparency and trust are paramount in government services. Rather than an arbitrary "87% match" score, CivicEase provides clear reasons: *"Matches your state (Maharashtra) and student status; check annual family income threshold (< 2.5 Lakhs)"*.
- **Alternatives Considered**: Opaque LLM-only matching was rejected because citizens need predictable, verifiable criteria without hallucinations.

#### Decision 2: Hybrid Offline-First Storage + Optional Firebase Cloud Sync
- **Chosen Approach**: All bookmarks, checklist states, and recent profile answers work locally via an in-memory & room/preferences layer. Users can connect their Google account at any time via Jetpack `CredentialManager` to back up and sync their saved schemes to Firebase Firestore.
- **Why**: Lowering the barrier to entry is critical for civic tools. Users must not be forced to sign in just to find out if they qualify for healthcare or scholarships.
- **Alternatives Considered**: Mandatory sign-in on first launch (rejected as high-friction for citizens).

#### Decision 3: AI Scheme Advisor with Gemini REST Service
- **Chosen Approach**: An embedded Gemini assistant configured with a specialized system prompt as a knowledgeable, empathetic Indian public policy guide. Explains complex terms (e.g., DBT, EWS certificates, DigiLocker requirements) and helps citizens find answers to specific edge cases.
- **Why**: Government rules have edge cases that static forms cannot anticipate.
- **Guardrails**: Always reminds citizens to verify final guidelines with official sources (`myscheme.gov.in`, `india.gov.in`) and never asks for sensitive IDs.

---

### 4. Technical Architecture & Data Strategy

#### System & Component Architecture

```
┌────────────────────────────────────────────────────────────────────────┐
│                              CivicEase App                             │
├────────────────────────────────────────────────────────────────────────┤
│                           MainActivity (M3)                            │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │                    CivicEaseNavigationGraph                      │  │
│  │ ┌───────────────┬────────────────┬───────────────┬─────────────┐ │  │
│  │ │ LandingScreen │  FinderScreen  │ ResultsScreen │ SavedScreen │ │  │
│  │ └───────┬───────┴────────┬───────┴───────┬───────┴──────┬──────┘ │  │
│  │         │                │               │              │        │  │
│  │         └────────────────┼───────────────┴──────────────┘        │  │
│  │                          ▼                                       │  │
│  │            ┌───────────────────────────┐                         │  │
│  │            │ SchemeDetailBottomSheet   │                         │  │
│  │            │  (Checklist + Apply Guide)│                         │  │
│  │            └───────────────────────────┘                         │  │
│  │                          ▼                                       │  │
│  │            ┌───────────────────────────┐                         │  │
│  │            │ SchemeAdvisorChatDialog   │                         │  │
│  │            │  (Gemini API Assistant)   │                         │  │
│  │            └───────────────────────────┘                         │  │
│  └──────────────────────────────────────────────────────────────────┘  │
├────────────────────────────────────────────────────────────────────────┤
│                          ViewModel & State Layer                       │
│  ┌───────────────────────┐  ┌─────────────────┐  ┌──────────────────┐  │
│  │ CivicEaseViewModel    │  │ SchemeAdvisorVM │  │ AuthViewModel    │  │
│  │ - questionnaireState  │  │ - chatMessages  │  │ - currentUser    │  │
│  │ - filteredSchemes     │  │ - isGenerating  │  │ - isSyncing      │  │
│  │ - bookmarks & checks  │  │                 │  │                  │  │
│  └───────────┬───────────┘  └────────┬────────┘  └────────┬─────────┘  │
├──────────────┼───────────────────────┼────────────────────┼────────────┤
│              ▼                       ▼                    ▼            │
│  ┌───────────────────────┐  ┌─────────────────┐  ┌──────────────────┐  │
│  │ SchemeRepository      │  │ GeminiService   │  │ FirebaseSyncRepo │  │
│  │ - Static Scheme DB    │  │ - gemini-flash  │  │ - Firestore      │  │
│  │ - Matching Engine     │  │   REST API      │  │ - CredentialMgr  │  │
│  │ - Local state store   │  │                 │  │   Google Sign-In │  │
│  └───────────────────────┘  └─────────────────┘  └──────────────────┘  │
└────────────────────────────────────────────────────────────────────────┘
```

#### Core Data Entities

1. **`Scheme`**:
   - `id: String`, `name: String`, `code: String`
   - `category: SchemeCategory` (Education, Healthcare, Agriculture, SocialSupport, Housing, Employment)
   - `level: SchemeLevel` (Central, State)
   - `applicableStates: List<String>` (or empty for Pan-India)
   - `department: String`, `briefSummary: String`, `targetBeneficiary: String`
   - `eligibilityCriteria: List<EligibilityCriterion>` (attribute, condition, description)
   - `benefitsOverview: String`, `requiredDocuments: List<String>`
   - `applicationSteps: List<String>`, `officialUrl: String`
   - `verifiedStatus: Boolean`, `lastVerifiedDate: String`

2. **`UserProfileAnswers`**:
   - `stateOrUT: String`
   - `ageGroup: AgeGroup`
   - `primarySector: SchemeCategory`
   - `occupationStatus: OccupationStatus`
   - `incomeBracket: IncomeBracket`
   - `specialCategories: Set<SpecialCategory>` (e.g. Small Farmer, Student, Disability, Minority, Female Head of Household)

3. **`SchemeMatchResult`**:
   - `scheme: Scheme`
   - `matchStatus: MatchStatus` (`POTENTIAL_MATCH`, `ONE_REQUIREMENT_UNCONFIRMED`, `MORE_INFO_NEEDED`)
   - `matchedReasons: List<String>`
   - `pendingVerification: List<String>`

4. **`SavedSchemeState` & `ChecklistProgress`**:
   - `isBookmarked: Boolean`
   - `completedDocumentIndices: Set<Int>`
   - `completedStepIndices: Set<Int>`
   - `notes: String`

---

### 5. Implementation Phases & Verification Steps

1. **Dependencies & Setup**:
   - Verify serialization, retrofit, compose material icons extended, and firebase libraries in `app/build.gradle.kts`.
   - Update `metadata.json` and `strings.xml` to set the official app name "CivicEase".
2. **Data & Matching Engine Layer**:
   - Implement authentic sample dataset with popular Indian schemes: *PM-Kisan Samman Nidhi*, *Ayushman Bharat (PM-JAY)*, *Post-Matric Scholarship*, *PM Awas Yojana (PMAY)*, *Atal Pension Yojana*, *National Apprenticeship Promotion Scheme (NAPS)*, *Samarth Scheme*, etc.
   - Implement rule matching engine that computes matched reasons and unconfirmed requirements with 100% transparency.
3. **UI Implementation**:
   - Landing page with hero banner, 3-step value proposition, sector cards, and quick actions.
   - Interactive 5-step benefits finder with step indicators, validation, and answer review.
   - Shortlist results screen with multi-filter chips, search query bar, and match breakdown tags.
   - Bottom sheet / detail drawer for deep scheme review, application steps, and interactive checklist.
   - Saved schemes screen showing checklist completion percentages.
   - AI Scheme Advisor bottom sheet or chat panel powered by Gemini REST client.
   - Google Sign-In button component for optional cloud backup to Firestore.
4. **Verification**:
   - Compile applet with `compile_applet`.
   - Ensure edge-to-edge insets, touch target compliance, clean error handling, and responsive layout.
