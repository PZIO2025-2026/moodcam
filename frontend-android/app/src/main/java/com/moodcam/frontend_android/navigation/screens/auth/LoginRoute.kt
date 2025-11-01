package com.moodcam.frontend_android.navigation.screens.auth

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moodcam.frontend_android.auth.ui.pages.LoginPage
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.navigation.Routes
import com.moodcam.frontend_android.navigation.helpers.navigateToHome

fun NavGraphBuilder.loginRoute(
    nav: NavHostController,
    authViewModel: AuthViewModel
) {
    composable(Routes.LOGIN) {
        LoginPage(
            modifier = Modifier,
            onHomeNavigate = { nav.navigateToHome() },
            onSignUpNavigate = {
                nav.navigate(Routes.SIGNUP) {
                    launchSingleTop = true
                }
            },
            authViewModel = authViewModel
        )
    }
}
