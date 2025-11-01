package com.moodcam.frontend_android.navigation.screens.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.navigation.Routes
import com.moodcam.frontend_android.navigation.helpers.AuthorizedScreen
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.profileRoute(nav: NavHostController) {
    composable(Routes.PROFILE) { navBackStackEntry ->
        val authViewModel: AuthViewModel = koinViewModel()
        
        AuthorizedScreen(authViewModel, nav) {
            ProfileScreenContent(
                navBackStackEntry = navBackStackEntry,
                onEditProfile = { nav.navigate(Routes.EDIT_PROFILE) }
            )
        }
    }
}
