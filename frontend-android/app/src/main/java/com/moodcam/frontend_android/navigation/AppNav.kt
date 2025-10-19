package com.moodcam.frontend_android.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moodcam.frontend_android.auth.ui.pages.LoginPage
import com.moodcam.frontend_android.auth.ui.pages.SignupPage
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.ui.camera.CameraScreen
import com.moodcam.frontend_android.ui.home.HomeScreen

@Composable
fun AppNav(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "login") {
        composable("home") {
            HomeScreen(
                navController = nav,
                authViewModel = authViewModel,

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
        composable("login") {
            LoginPage(modifier, nav, authViewModel)
        }
        composable("signup") {
            SignupPage(modifier, nav, authViewModel)
        }
    }
}

@Composable
private fun SimpleTextScreen(text: String) {
    Text(text)
}

@Preview
@Composable
private fun AppNavPreview(modifier: Modifier = Modifier, authViewModel: AuthViewModel = AuthViewModel()) {
    AppNav(modifier, authViewModel)
}
