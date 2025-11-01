package com.moodcam.frontend_android.navigation.screens.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.navigation.Routes
import com.moodcam.frontend_android.navigation.helpers.AuthorizedScreen
import com.moodcam.frontend_android.ui.camera.CameraScreen

fun NavGraphBuilder.cameraRoute(
    nav: NavHostController,
    authViewModel: AuthViewModel
) {
    composable(Routes.CAMERA) {
        AuthorizedScreen(authViewModel, nav) {
            CameraScreen(
                onNavigateUp = { nav.navigateUp() },
                authViewModel = authViewModel
            )
        }
    }
}
