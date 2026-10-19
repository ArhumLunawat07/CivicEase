package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.ManageSearch
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.MintContainer
import com.example.ui.theme.SlateMuted
import com.example.ui.viewmodel.AppNavScreen

@Composable
fun CivicEaseBottomNav(
  currentScreen: AppNavScreen,
  savedCount: Int,
  onNavigate: (AppNavScreen) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier,
    windowInsets = WindowInsets.navigationBars,
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 6.dp
  ) {
    // 1. Discover Tab
    NavigationBarItem(
      selected = currentScreen == AppNavScreen.LANDING,
      onClick = { onNavigate(AppNavScreen.LANDING) },
      icon = {
        Icon(
          imageVector = if (currentScreen == AppNavScreen.LANDING) Icons.Filled.Explore else Icons.Outlined.Explore,
          contentDescription = "Discover",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text("Discover", fontSize = 11.sp, fontWeight = if (currentScreen == AppNavScreen.LANDING) FontWeight.Bold else FontWeight.Normal)
      },
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = MintContainer,
        selectedIconColor = ForestGreenPrimary,
        selectedTextColor = ForestGreenPrimary,
        unselectedIconColor = SlateMuted,
        unselectedTextColor = SlateMuted
      ),
      modifier = Modifier.testTag("nav_tab_discover")
    )

    // 2. Finder Tab
    NavigationBarItem(
      selected = currentScreen == AppNavScreen.FINDER,
      onClick = { onNavigate(AppNavScreen.FINDER) },
      icon = {
        Icon(
          imageVector = if (currentScreen == AppNavScreen.FINDER) Icons.Filled.ManageSearch else Icons.Outlined.ManageSearch,
          contentDescription = "Benefits Finder",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text("Finder", fontSize = 11.sp, fontWeight = if (currentScreen == AppNavScreen.FINDER) FontWeight.Bold else FontWeight.Normal)
      },
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = MintContainer,
        selectedIconColor = ForestGreenPrimary,
        selectedTextColor = ForestGreenPrimary,
        unselectedIconColor = SlateMuted,
        unselectedTextColor = SlateMuted
      ),
      modifier = Modifier.testTag("nav_tab_finder")
    )

    // 3. Shortlist Tab
    NavigationBarItem(
      selected = currentScreen == AppNavScreen.RESULTS,
      onClick = { onNavigate(AppNavScreen.RESULTS) },
      icon = {
        Icon(
          imageVector = if (currentScreen == AppNavScreen.RESULTS) Icons.Filled.Search else Icons.Outlined.Search,
          contentDescription = "Shortlist",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text("Shortlist", fontSize = 11.sp, fontWeight = if (currentScreen == AppNavScreen.RESULTS) FontWeight.Bold else FontWeight.Normal)
      },
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = MintContainer,
        selectedIconColor = ForestGreenPrimary,
        selectedTextColor = ForestGreenPrimary,
        unselectedIconColor = SlateMuted,
        unselectedTextColor = SlateMuted
      ),
      modifier = Modifier.testTag("nav_tab_shortlist")
    )

    // 4. Saved & Checklist Tab
    NavigationBarItem(
      selected = currentScreen == AppNavScreen.SAVED,
      onClick = { onNavigate(AppNavScreen.SAVED) },
      icon = {
        BadgedBox(
          badge = {
            if (savedCount > 0) {
              Badge(
                containerColor = ForestGreenPrimary
              ) {
                Text(text = "$savedCount", fontSize = 9.sp)
              }
            }
          }
        ) {
          Icon(
            imageVector = if (currentScreen == AppNavScreen.SAVED) Icons.Filled.Checklist else Icons.Outlined.Checklist,
            contentDescription = "Saved & Checklist",
            modifier = Modifier.size(24.dp)
          )
        }
      },
      label = {
        Text("Checklist", fontSize = 11.sp, fontWeight = if (currentScreen == AppNavScreen.SAVED) FontWeight.Bold else FontWeight.Normal)
      },
      colors = NavigationBarItemDefaults.colors(
        indicatorColor = MintContainer,
        selectedIconColor = ForestGreenPrimary,
        selectedTextColor = ForestGreenPrimary,
        unselectedIconColor = SlateMuted,
        unselectedTextColor = SlateMuted
      ),
      modifier = Modifier.testTag("nav_tab_saved")
    )
  }
}
