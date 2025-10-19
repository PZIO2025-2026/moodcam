package com.moodcam.frontend_android.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moodcam.frontend_android.ui.camera.CameraScreen
import com.moodcam.frontend_android.ui.home.HomeScreen

@Composable
fun AppNav() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onOpenCamera = { nav.navigate("camera") },
                onOpenGallery = { nav.navigate("gallery") },
                onOpenHistory = { nav.navigate("history") }
            )
        }
        composable("camera") {
            CameraScreen()
        }
        composable("gallery") {
            // TODO: Implement gallery screen
            SimpleTextScreen("Gallery (TODO)")
        }
        composable("history") {
            // TODO: Implement history screen
            SimpleTextScreen("History (TODO)")
        }
    }
}

@Composable
private fun SimpleTextScreen(text: String) {
    Text(text)
}

@Preview
@Composable
private fun AppNavPreview() {
    AppNav()
}
