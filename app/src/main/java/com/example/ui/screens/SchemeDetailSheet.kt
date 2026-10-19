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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SavedSchemeState
import com.example.data.model.Scheme
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchemeDetailSheet(
  scheme: Scheme,
  savedState: SavedSchemeState?,
  onToggleBookmark: () -> Unit,
  onToggleDocumentCheck: (Int) -> Unit,
  onToggleStepCheck: (Int) -> Unit,
  onOpenAdvisorWithScheme: (String) -> Unit,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val isBookmarked = savedState != null
  val completedDocs = savedState?.completedDocIndices ?: emptySet()
  val completedSteps = savedState?.completedStepIndices ?: emptySet()

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = WarmSurface,
    dragHandle = { BottomSheetDefaults.DragHandle() },
    modifier = Modifier.testTag("scheme_detail_sheet")
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
      contentPadding = PaddingValues(bottom = 48.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Header & Actions
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Top
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = MintContainer
              ) {
                Text(
                  text = scheme.category.displayName,
                  color = ForestGreenPrimary,
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = WarmSurfaceVariant
              ) {
                Text(
                  text = scheme.level.displayName,
                  color = SlateMuted,
                  fontSize = 11.sp,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = scheme.name,
              fontWeight = FontWeight.Bold,
              fontSize = 19.sp,
              color = DeepNavyText,
              lineHeight = 25.sp
            )

            Text(
              text = scheme.department,
              fontSize = 12.sp,
              color = SlateMuted,
              modifier = Modifier.padding(top = 2.dp)
            )
          }

          IconButton(
            onClick = onToggleBookmark,
            modifier = Modifier.size(44.dp)
          ) {
            Icon(
              imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
              contentDescription = "Save Scheme",
              tint = if (isBookmarked) ForestGreenPrimary else SlateLight,
              modifier = Modifier.size(26.dp)
            )
          }
        }
      }

      // 2. Who this scheme helps
      item {
        DetailSectionCard(title = "Who this scheme is intended to help", icon = Icons.Outlined.People) {
          Text(
            text = scheme.targetBeneficiary,
            fontSize = 13.sp,
            color = DeepNavyText,
            lineHeight = 18.sp
          )
        }
      }

      // 3. Benefits Overview
      item {
        DetailSectionCard(title = "Key Potential Benefits", icon = Icons.Outlined.Payments) {
          Text(
            text = scheme.benefitsOverview,
            fontSize = 13.sp,
            color = DeepNavyText,
            lineHeight = 18.sp
          )
        }
      }

      // 4. Eligibility Conditions
      item {
        DetailSectionCard(title = "Eligibility Conditions", icon = Icons.Outlined.FactCheck) {
          scheme.eligibilityCriteria.forEachIndexed { idx, crit ->
            Row(
              modifier = Modifier.padding(vertical = 4.dp),
              verticalAlignment = Alignment.Top
            ) {
              Text("• ", fontWeight = FontWeight.Bold, color = ForestGreenPrimary, fontSize = 14.sp)
              Column {
                Text(
                  text = crit.title,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = DeepNavyText
                )
                Text(
                  text = crit.explanation,
                  fontSize = 12.sp,
                  color = SlateMuted,
                  lineHeight = 16.sp
                )
              }
            }
          }
        }
      }

      // 5. Interactive Document Checklist
      item {
        DetailSectionCard(
          title = "Required Documents Checklist",
          subtitle = "Mark each document as you gather it. Progress is saved locally.",
          icon = Icons.Outlined.Description
        ) {
          scheme.requiredDocuments.forEachIndexed { docIdx, docName ->
            val isChecked = completedDocs.contains(docIdx)
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isChecked) MintContainer.copy(alpha = 0.5f) else WarmSurfaceVariant,
              border = androidx.compose.foundation.BorderStroke(1.dp, if (isChecked) ForestGreenPrimary else WarmBorder),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
                .clickable { onToggleDocumentCheck(docIdx) }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Checkbox(
                  checked = isChecked,
                  onCheckedChange = { onToggleDocumentCheck(docIdx) },
                  colors = CheckboxDefaults.colors(checkedColor = ForestGreenPrimary)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = docName,
                  fontSize = 13.sp,
                  fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal,
                  color = DeepNavyText
                )
              }
            }
          }
        }
      }

      // 6. Step-by-Step Application Instructions
      item {
        DetailSectionCard(
          title = "Step-by-Step Application Instructions",
          subtitle = "Follow these numbered stages to apply on the designated portal or offline centre.",
          icon = Icons.Outlined.FormatListNumbered
        ) {
          scheme.applicationSteps.forEachIndexed { stepIdx, instruction ->
            val isStepDone = completedSteps.contains(stepIdx)
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.Top
            ) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .clip(CircleShape)
                  .background(if (isStepDone) ForestGreenPrimary else MintContainer)
                  .clickable { onToggleStepCheck(stepIdx) },
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "${stepIdx + 1}",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isStepDone) Color.White else ForestGreenPrimary
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = instruction,
                  fontSize = 13.sp,
                  color = DeepNavyText,
                  lineHeight = 18.sp
                )
              }
              Checkbox(
                checked = isStepDone,
                onCheckedChange = { onToggleStepCheck(stepIdx) },
                colors = CheckboxDefaults.colors(checkedColor = ForestGreenPrimary)
              )
            }
          }
        }
      }

      // 7. Official Source & Application Portal Link
      item {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = WarmSurfaceVariant),
          border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Launch,
                contentDescription = null,
                tint = ForestGreenPrimary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Official Scheme Portal",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DeepNavyText
              )
            }
            Text(
              text = "Last verified: ${scheme.lastVerifiedDate}. Submit official applications and verify status exclusively via designated portal.",
              fontSize = 11.sp,
              color = SlateMuted,
              modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
            )

            Button(
              onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(scheme.officialUrl))
                context.startActivity(intent)
              },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("Open Official Website ↗", fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // 8. Ask AI Advisor Quick Action
      item {
        FilledTonalButton(
          onClick = {
            onDismiss()
            onOpenAdvisorWithScheme("Can you explain the detailed eligibility criteria and document verification steps for ${scheme.name}?")
          },
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = AmberContainer,
            contentColor = AmberHighlight
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Ask AI Advisor About This Scheme", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
      }
    }
  }
}

@Composable
fun DetailSectionCard(
  title: String,
  subtitle: String? = null,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  content: @Composable ColumnScope.() -> Unit
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = WarmSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = ForestGreenPrimary,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = title,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = DeepNavyText
        )
      }
      if (!subtitle.isNullOrBlank()) {
        Text(
          text = subtitle,
          fontSize = 11.sp,
          color = SlateMuted,
          modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
        )
      } else {
        Spacer(modifier = Modifier.height(8.dp))
      }
      content()
    }
  }
}
