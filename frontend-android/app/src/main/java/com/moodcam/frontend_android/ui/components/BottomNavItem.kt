package com.moodcam.frontend_android.ui.components

import androidx.compose.ui.graphics.vector.ImageVector

/** Data model describing a bottom navigation destination. */
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)