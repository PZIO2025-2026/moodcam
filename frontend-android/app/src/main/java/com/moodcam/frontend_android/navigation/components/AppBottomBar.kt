package com.moodcam.frontend_android.navigation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.moodcam.frontend_android.navigation.Routes
import com.moodcam.frontend_android.ui.components.BottomNavItem

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Default.Home, Routes.HOME),
    BottomNavItem("History", Icons.Default.List, Routes.HISTORY),
    BottomNavItem("Gallery", Icons.Default.PhotoLibrary, Routes.GALLERY),
    BottomNavItem("Profile", Icons.Default.AccountCircle, Routes.PROFILE)
)

@Composable
fun AppBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF1A1625)
    ) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF6C63FF),
                    selectedTextColor = Color(0xFF6C63FF),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color(0xFF2D2640)
                )
            )
        }
    }
}
