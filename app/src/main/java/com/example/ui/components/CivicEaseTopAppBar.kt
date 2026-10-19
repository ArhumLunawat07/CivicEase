package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CivicEaseTopAppBar(
  savedCount: Int,
  onOpenAdvisor: () -> Unit,
  onOpenSaved: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    shadowElevation = 1.dp
  ) {
    Row(
      modifier = Modifier
        .statusBarsPadding()
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Left: Logo and Brand Name
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(ForestGreenPrimary),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_civicease_logo_1792408273814),
            contentDescription = "CivicEase Logo",
            modifier = Modifier.size(34.dp)
          )
        }

        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "CivicEase",
              fontWeight = FontWeight.Bold,
              fontSize = 19.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = MintContainer
            ) {
              Text(
                text = "INDIA",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
              )
            }
          }
          Text(
            text = "Public Benefits Discovery",
            fontSize = 11.sp,
            color = SlateMuted
          )
        }
      }

      // Right: Actions
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // AI Scheme Advisor Button
        FilledTonalButton(
          onClick = onOpenAdvisor,
          shape = RoundedCornerShape(20.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
          colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = AmberContainer,
            contentColor = AmberHighlight
          ),
          modifier = Modifier
            .defaultMinSize(minHeight = 36.dp)
            .testTag("ai_advisor_top_button")
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "AI Advisor",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        // Saved Schemes Count Action
        IconButton(
          onClick = onOpenSaved,
          modifier = Modifier
            .size(40.dp)
            .testTag("saved_schemes_top_button")
        ) {
          BadgedBox(
            badge = {
              if (savedCount > 0) {
                Badge(
                  containerColor = ForestGreenPrimary,
                  contentColor = Color.White
                ) {
                  Text(text = "$savedCount", fontSize = 10.sp)
                }
              }
            }
          ) {
            Icon(
              imageVector = if (savedCount > 0) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
              contentDescription = "Saved Schemes",
              tint = if (savedCount > 0) ForestGreenPrimary else SlateMuted,
              modifier = Modifier.size(24.dp)
            )
          }
        }
      }
    }
  }
}
