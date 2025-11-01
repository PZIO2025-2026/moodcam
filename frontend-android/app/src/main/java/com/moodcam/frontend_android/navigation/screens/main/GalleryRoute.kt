package com.moodcam.frontend_android.navigation.screens.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.navigation.Routes
import com.moodcam.frontend_android.navigation.helpers.AuthorizedScreen
import com.moodcam.frontend_android.ui.components.SimpleTextScreen

fun NavGraphBuilder.galleryRoute(
    nav: NavHostController,
    authViewModel: AuthViewModel
) {
    composable(Routes.GALLERY) {
        AuthorizedScreen(authViewModel, nav) {
            SimpleTextScreen("Gallery (TODO)")
        }
    }
}
