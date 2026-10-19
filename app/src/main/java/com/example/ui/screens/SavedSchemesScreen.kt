package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SavedSchemeState
import com.example.data.model.Scheme
import com.example.data.repository.SchemeRepository
import com.example.ui.theme.*

@Composable
fun SavedSchemesScreen(
  savedMap: Map<String, SavedSchemeState>,
  isGoogleConnected: Boolean,
  userEmail: String?,
  onConnectGoogle: () -> Unit,
  onDisconnectGoogle: () -> Unit,
  onOpenSchemeDetail: (Scheme) -> Unit,
  onToggleBookmark: (String) -> Unit,
  onToggleDocumentCheck: (String, Int) -> Unit,
  onExploreSchemes: () -> Unit,
  modifier: Modifier = Modifier
) {
  val savedSchemes = SchemeRepository.SCHEMES.filter { savedMap.containsKey(it.id) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(WarmBackground),
    contentPadding = PaddingValues(bottom = 96.dp)
  ) {
    // 1. HEADER
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(WarmSurface)
          .padding(horizontal = 20.dp, vertical = 14.dp)
      ) {
        Text(
          text = "Saved Schemes & Checklist",
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = DeepNavyText
        )
        Text(
          text = "${savedSchemes.size} programs bookmarked for application preparation",
          fontSize = 13.sp,
          color = SlateMuted
        )
      }
    }

    // 2. OPTIONAL GOOGLE SYNC CARD
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 10.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CloudSync,
                contentDescription = null,
                tint = ForestGreenPrimary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (isGoogleConnected) "Cloud Sync Active" else "Optional Google Cloud Sync",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DeepNavyText
              )
            }

            if (isGoogleConnected) {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = MatchSuccessContainer
              ) {
                Text(
                  text = "CONNECTED",
                  color = MatchSuccess,
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = if (isGoogleConnected) {
              "Bookmarks and checklist progress are backed up and synced for ${userEmail ?: "your Google account"}."
            } else {
              "You are in Guest Mode. Your progress is saved safely on this device. Sign in with Google anytime to sync across devices."
            },
            fontSize = 12.sp,
            color = SlateMuted,
            lineHeight = 16.sp
          )

          Spacer(modifier = Modifier.height(10.dp))

          if (!isGoogleConnected) {
            Button(
              onClick = onConnectGoogle,
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
              modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .testTag("google_sync_connect_button")
            ) {
              Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text("Sync with Google Account", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
          } else {
            OutlinedButton(
              onClick = onDisconnectGoogle,
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
            ) {
              Text("Disconnect Account", fontSize = 12.sp, color = SlateMuted)
            }
          }
        }
      }
    }

    // 3. SAVED SCHEMES LIST OR EMPTY STATE
    if (savedSchemes.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = WarmSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.BookmarkBorder,
              contentDescription = null,
              tint = SlateLight,
              modifier = Modifier.size(48.dp)
            )
            Text(
              text = "No saved schemes yet",
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = DeepNavyText
            )
            Text(
              text = "Bookmark programs from your benefits shortlist to track required documents and application steps in one place.",
              fontSize = 13.sp,
              color = SlateMuted,
              lineHeight = 18.sp,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center,
              modifier = Modifier.padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Button(
              onClick = onExploreSchemes,
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
            ) {
              Text("Discover Schemes", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    } else {
      items(savedSchemes, key = { it.id }) { scheme ->
        val state = savedMap[scheme.id]
        val totalDocs = scheme.requiredDocuments.size
        val completedCount = state?.completedDocIndices?.size ?: 0
        val progress = if (totalDocs > 0) completedCount.toFloat() / totalDocs.toFloat() else 0f

        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = WarmSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
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

              IconButton(
                onClick = { onToggleBookmark(scheme.id) },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.BookmarkRemove,
                  contentDescription = "Remove bookmark",
                  tint = MaterialTheme.colorScheme.error,
                  modifier = Modifier.size(20.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = scheme.name,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = DeepNavyText
            )

            Text(
              text = scheme.department,
              fontSize = 11.sp,
              color = SlateMuted,
              modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
            )

            // Checklist Progress Bar
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Document Checklist",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = DeepNavyText
              )
              Text(
                text = "$completedCount of $totalDocs ready (${(progress * 100).toInt()}%)",
                fontSize = 11.sp,
                color = if (completedCount == totalDocs) MatchSuccess else ForestGreenPrimary,
                fontWeight = FontWeight.Bold
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
              progress = { progress },
              color = if (completedCount == totalDocs) MatchSuccess else ForestGreenPrimary,
              trackColor = WarmBorder,
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Checklist items
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              scheme.requiredDocuments.forEachIndexed { idx, docName ->
                val isDone = state?.completedDocIndices?.contains(idx) == true
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleDocumentCheck(scheme.id, idx) }
                    .padding(vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Checkbox(
                    checked = isDone,
                    onCheckedChange = { onToggleDocumentCheck(scheme.id, idx) },
                    colors = CheckboxDefaults.colors(checkedColor = ForestGreenPrimary),
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = docName,
                    fontSize = 12.sp,
                    color = if (isDone) SlateLight else DeepNavyText,
                    lineHeight = 16.sp
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // View full details button
            OutlinedButton(
              onClick = { onOpenSchemeDetail(scheme) },
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("View Full Scheme & Steps ↗", fontSize = 12.sp, color = DeepNavyText, fontWeight = FontWeight.SemiBold)
            }
          }
        }
      }
    }
  }
}
