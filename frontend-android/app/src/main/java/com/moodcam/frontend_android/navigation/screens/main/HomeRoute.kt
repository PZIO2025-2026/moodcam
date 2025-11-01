package com.moodcam.frontend_android.navigation.screens.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.navigation.Routes
import com.moodcam.frontend_android.navigation.helpers.AuthorizedScreen
import com.moodcam.frontend_android.ui.home.HomeScreen

fun NavGraphBuilder.homeRoute(
    nav: NavHostController,
    authViewModel: AuthViewModel
) {
    composable(Routes.HOME) {
        AuthorizedScreen(authViewModel, nav) {
            HomeScreen(onOpenCamera = { nav.navigate(Routes.CAMERA) })
        }
    }
}
