/** Registers the login route. */

package com.moodcam.frontend_android.navigation.screens.auth

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moodcam.frontend_android.auth.ui.pages.LoginPage
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.navigation.Routes
import com.moodcam.frontend_android.navigation.helpers.navigateToHome
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.loginRoute(nav: NavHostController) {
    /**
     * Registers the login screen route.
     * Provides navigation to home on success or to signup.
     *
     * @param nav Host controller used for navigation actions.
     */
    composable(Routes.LOGIN) {
        val authViewModel: AuthViewModel = koinViewModel()
        
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
