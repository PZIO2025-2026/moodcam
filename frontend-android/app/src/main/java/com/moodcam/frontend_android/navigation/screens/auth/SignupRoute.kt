package com.moodcam.frontend_android.navigation.screens.auth

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moodcam.frontend_android.auth.ui.pages.SignupPage
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.navigation.Routes
import com.moodcam.frontend_android.navigation.helpers.navigateToHome
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.signupRoute(nav: NavHostController) {
    composable(Routes.SIGNUP) {
        val authViewModel: AuthViewModel = koinViewModel()
        
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
