package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SchemeCategory
import com.example.data.repository.SchemeRepository
import com.example.ui.theme.*

@Composable
fun LandingScreen(
  onStartFinder: () -> Unit,
  onExploreAllSchemes: () -> Unit,
  onSelectCategory: (SchemeCategory) -> Unit,
  onOpenAdvisor: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(WarmBackground),
    contentPadding = PaddingValues(bottom = 96.dp)
  ) {
    // 1. HERO SECTION
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                MintContainer.copy(alpha = 0.5f),
                WarmBackground
              )
            )
          )
          .padding(horizontal = 20.dp, vertical = 24.dp)
      ) {
        Column(
          horizontalAlignment = Alignment.Start,
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Pill tag
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = ForestGreenPrimary.copy(alpha = 0.1f),
            border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreenPrimary.copy(alpha = 0.2f))
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = ForestGreenPrimary,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Independent Benefits Discovery Guide",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = ForestGreenPrimary
              )
            }
          }

          // Main Headline
          Text(
            text = "You could be eligible for more support than you think.",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = DeepNavyText,
            lineHeight = 36.sp
          )

          // Subtitle
          Text(
            text = "CivicEase turns complex public welfare schemes into a calm, plain-language discovery experience. Find out which central and state programs may fit your needs, what documents to gather, and exactly where to apply.",
            fontSize = 15.sp,
            color = SlateMuted,
            lineHeight = 22.sp
          )

          // Primary & Secondary CTAs
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Button(
              onClick = onStartFinder,
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = ForestGreenPrimary,
                contentColor = Color.White
              ),
              modifier = Modifier
                .weight(1.2f)
                .height(52.dp)
                .testTag("landing_find_benefits_cta")
            ) {
              Icon(
                imageVector = Icons.Default.ManageSearch,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Find benefits for me",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }

            OutlinedButton(
              onClick = onExploreAllSchemes,
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.outlinedButtonColors(
                contentColor = DeepNavyText
              ),
              border = androidx.compose.foundation.BorderStroke(1.5.dp, ForestGreenPrimary.copy(alpha = 0.4f)),
              modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .testTag("landing_explore_schemes_cta")
            ) {
              Text(
                text = "Explore schemes",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
              )
            }
          }

          // Privacy Assurance Banner
          Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = WarmSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = ForestGreenPrimary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = "100% Private & Anonymous: We never ask for Aadhaar numbers, bank accounts, or identity proofs.",
                fontSize = 12.sp,
                color = SlateMuted,
                lineHeight = 16.sp
              )
            }
          }
        }
      }
    }

    // 2. TASTEFUL VISUAL REPRESENTATION: BENEFIT PREVIEW CARDS
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 8.dp)
      ) {
        Text(
          text = "Featured National Welfare Programs",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = DeepNavyText
        )
        Text(
          text = "Direct insights from official central guidelines",
          fontSize = 13.sp,
          color = SlateMuted
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Card 1: Ayushman Bharat
        FeaturedCardPreview(
          title = "Ayushman Bharat (PM-JAY)",
          category = "Healthcare",
          highlight = "Up to ₹5 Lakh / year secondary & tertiary care cover",
          badgeColor = MatchSuccess,
          badgeBg = MatchSuccessContainer,
          onClick = onExploreAllSchemes
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Card 2: PM-Kisan
        FeaturedCardPreview(
          title = "PM-Kisan Samman Nidhi",
          category = "Agriculture",
          highlight = "₹6,000 annual direct benefit transfer in 3 installments",
          badgeColor = MatchInfo,
          badgeBg = MatchInfoContainer,
          onClick = onExploreAllSchemes
        )
      }
    }

    // 3. THREE-STEP PROCESS EXPLANATION
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 20.dp)
      ) {
        Text(
          text = "How CivicEase Works",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = DeepNavyText
        )
        Text(
          text = "A transparent, 3-step path from confusion to action",
          fontSize = 13.sp,
          color = SlateMuted
        )
        Spacer(modifier = Modifier.height(16.dp))

        StepExplanationCard(
          stepNumber = "1",
          title = "Tell us about your circumstances",
          description = "Answer 5 brief, non-intrusive questions regarding your location, occupation, and reason for seeking support. No account creation required.",
          icon = Icons.Outlined.PersonPinCircle
        )

        Spacer(modifier = Modifier.height(12.dp))

        StepExplanationCard(
          stepNumber = "2",
          title = "Discover relevant schemes",
          description = "CivicEase runs a deterministic rules engine showing why each program may fit and flagging any specific criteria you need to confirm.",
          icon = Icons.Outlined.CheckCircleOutline
        )

        Spacer(modifier = Modifier.height(12.dp))

        StepExplanationCard(
          stepNumber = "3",
          title = "Understand your next steps",
          description = "Access plain-language application instructions, an interactive required-document checklist, and direct links to official portals.",
          icon = Icons.Outlined.PlaylistAddCheck
        )
      }
    }

    // 4. BENEFIT CATEGORY SECTORS
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Explore by Sector",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = DeepNavyText
            )
            Text(
              text = "Verified schemes across major categories",
              fontSize = 13.sp,
              color = SlateMuted
            )
          }

          TextButton(onClick = onExploreAllSchemes) {
            Text("See all (${SchemeRepository.SCHEMES.size})", color = ForestGreenPrimary, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grid of Category Cards
        val categories = SchemeCategory.values()
        for (i in categories.indices step 2) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            CategoryTile(
              category = categories[i],
              modifier = Modifier.weight(1f),
              onClick = { onSelectCategory(categories[i]) }
            )
            if (i + 1 < categories.size) {
              CategoryTile(
                category = categories[i + 1],
                modifier = Modifier.weight(1f),
                onClick = { onSelectCategory(categories[i + 1]) }
              )
            } else {
              Spacer(modifier = Modifier.weight(1f))
            }
          }
          Spacer(modifier = Modifier.height(12.dp))
        }
      }
    }

    // 5. AI SCHEME ADVISOR BANNER
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AmberContainer.copy(alpha = 0.5f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, AmberHighlight.copy(alpha = 0.3f)),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 12.dp)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(48.dp)
              .clip(CircleShape)
              .background(AmberHighlight),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(26.dp)
            )
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Have questions about eligibility?",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = DeepNavyText
            )
            Text(
              text = "Ask our AI Scheme Advisor about DigiLocker documents, EWS ceilings, or DBT bank seeding.",
              fontSize = 12.sp,
              color = SlateMuted,
              lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
              onClick = onOpenAdvisor,
              colors = ButtonDefaults.buttonColors(containerColor = AmberHighlight),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
              modifier = Modifier.height(36.dp)
            ) {
              Text("Ask AI Advisor", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // 6. TRUST & VERIFICATION NOTICE (No fake claims)
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 12.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.VerifiedUser,
              contentDescription = null,
              tint = ForestGreenPrimary,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "Trust & Transparency Pledge",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = DeepNavyText
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "• Informational Discovery Only: CivicEase is an independent civic-tech platform designed to simplify scheme discovery. We do not represent or act on behalf of the Government of India.\n" +
                   "• Official Authority: Official scheme rules, verification by designated nodal officers, and supporting documents determine final eligibility.\n" +
                   "• Direct Official Portals: All application submissions must occur via designated official portals such as myScheme.gov.in and india.gov.in.",
            fontSize = 13.sp,
            color = SlateMuted,
            lineHeight = 19.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedButton(
              onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.myscheme.gov.in/"))
                context.startActivity(intent)
              },
              modifier = Modifier.weight(1f)
            ) {
              Text("myScheme.gov.in ↗", fontSize = 12.sp)
            }

            OutlinedButton(
              onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.india.gov.in/"))
                context.startActivity(intent)
              },
              modifier = Modifier.weight(1f)
            ) {
              Text("india.gov.in ↗", fontSize = 12.sp)
            }
          }
        }
      }
    }

    // 7. FOOTER
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "CivicEase",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          color = DeepNavyText
        )
        Text(
          text = "“The support you deserve, made easier to find.”",
          fontSize = 13.sp,
          fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
          color = SlateMuted
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Version 1.0 • Built with open civic data & privacy-by-design",
          fontSize = 11.sp,
          color = SlateLight
        )
      }
    }
  }
}

@Composable
fun FeaturedCardPreview(
  title: String,
  category: String,
  highlight: String,
  badgeColor: Color,
  badgeBg: Color,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = WarmSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = badgeBg
          ) {
            Text(
              text = category,
              color = badgeColor,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Central Govt",
            fontSize = 11.sp,
            color = SlateLight
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = title,
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = DeepNavyText
        )
        Text(
          text = highlight,
          fontSize = 12.sp,
          color = SlateMuted,
          lineHeight = 16.sp
        )
      }
      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = null,
        tint = SlateLight
      )
    }
  }
}

@Composable
fun StepExplanationCard(
  stepNumber: String,
  title: String,
  description: String,
  icon: ImageVector
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = WarmSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.Top
    ) {
      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(CircleShape)
          .background(MintContainer),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = stepNumber,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 14.sp,
          color = ForestGreenPrimary
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ForestGreenPrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = DeepNavyText
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = description,
          fontSize = 12.sp,
          color = SlateMuted,
          lineHeight = 17.sp
        )
      }
    }
  }
}

@Composable
fun CategoryTile(
  category: SchemeCategory,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  val icon = when (category) {
    SchemeCategory.EDUCATION -> Icons.Outlined.School
    SchemeCategory.HEALTHCARE -> Icons.Outlined.MedicalServices
    SchemeCategory.AGRICULTURE -> Icons.Outlined.Agriculture
    SchemeCategory.SOCIAL_SUPPORT -> Icons.Outlined.VolunteerActivism
    SchemeCategory.HOUSING -> Icons.Outlined.Home
    SchemeCategory.EMPLOYMENT -> Icons.Outlined.WorkOutline
  }

  val schemeCount = SchemeRepository.SCHEMES.count { it.category == category }

  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = WarmSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
    modifier = modifier
      .clickable(onClick = onClick)
      .testTag("category_tile_${category.name.lowercase()}")
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(MintContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = ForestGreenPrimary,
          modifier = Modifier.size(20.dp)
        )
      }

      Text(
        text = category.displayName,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = DeepNavyText
      )

      Text(
        text = "$schemeCount schemes available",
        fontSize = 11.sp,
        color = SlateMuted
      )
    }
  }
}
