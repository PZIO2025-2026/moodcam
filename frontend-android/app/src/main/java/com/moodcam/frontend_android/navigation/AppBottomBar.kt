package com.moodcam.frontend_android.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppBottomBar(
    onNavigateClicked: (route: String) -> Unit,
    currentRoute: String?
) {
    NavigationBar(
        containerColor = Color(0xFF1A1625),
        contentColor = Color.White
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentRoute?.let {
                item.route == it
            } ?: false

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onNavigateClicked(item.route)
                },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected)
                            Color(0xFF8B5CF6)
                        else
                            Color.White.copy(alpha = 0.6f)
                    )
                },
                label = {
                    Text(
                        item.label,
                        color = if (isSelected)
                            Color(0xFF8B5CF6)
                        else
                            Color.White.copy(alpha = 0.6f)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF8B5CF6),
                    selectedTextColor = Color(0xFF8B5CF6),
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color(0xFF8B5CF6).copy(alpha = 0.2f)
                )
            )
        }
    }
}