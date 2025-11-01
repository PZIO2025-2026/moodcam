package com.moodcam.frontend_android.navigation.screens.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.UserRepository
import com.moodcam.frontend_android.navigation.Routes
import com.moodcam.frontend_android.navigation.helpers.AuthorizedScreen

fun NavGraphBuilder.profileRoute(
    nav: NavHostController,
    authViewModel: AuthViewModel,
    userRepository: UserRepository
) {
    composable(Routes.PROFILE) { navBackStackEntry ->
        AuthorizedScreen(authViewModel, nav) {
            ProfileScreenContent(
                authViewModel = authViewModel,
                userRepository = userRepository,
                navBackStackEntry = navBackStackEntry,
                onEditProfile = { nav.navigate(Routes.EDIT_PROFILE) }
            )
        }
    }
}
