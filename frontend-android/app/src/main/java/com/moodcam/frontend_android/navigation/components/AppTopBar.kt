/** Center-aligned top app bar showing title and conditional close action. */

package com.moodcam.frontend_android.navigation.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.navigation.NavHostController
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navController: NavHostController, currentRoute: String?) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "MoodCam",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            if (currentRoute == com.moodcam.frontend_android.navigation.Routes.SETTINGS) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF1A1625)
        )
    )
}
