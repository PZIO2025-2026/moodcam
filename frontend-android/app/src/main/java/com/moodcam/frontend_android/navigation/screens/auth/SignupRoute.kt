package com.moodcam.frontend_android.navigation.screens.auth

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moodcam.frontend_android.auth.ui.pages.SignupPage
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.navigation.Routes
import com.moodcam.frontend_android.navigation.helpers.navigateToHome

fun NavGraphBuilder.signupRoute(
    nav: NavHostController,
    authViewModel: AuthViewModel
) {
    composable(Routes.SIGNUP) {
        SignupPage(
            modifier = Modifier,
            onHomeNavigate = { nav.navigateToHome() },
            onLoginNavigate = {
                nav.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SIGNUP) { inclusive = true }
                    launchSingleTop = true
                }
            },
            authViewModel = authViewModel
        )
    }
}
