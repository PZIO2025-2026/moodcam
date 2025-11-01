package com.moodcam.frontend_android.navigation.screens.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.UserRepository
import com.moodcam.frontend_android.navigation.Routes
import com.moodcam.frontend_android.navigation.helpers.AuthorizedScreen

fun NavGraphBuilder.editProfileRoute(
    nav: NavHostController,
    authViewModel: AuthViewModel,
    userRepository: UserRepository
) {
    composable(Routes.EDIT_PROFILE) {
        AuthorizedScreen(authViewModel, nav) {
            EditProfileScreenContent(
                authViewModel = authViewModel,
                userRepository = userRepository,
                onSaveComplete = {
                    nav.previousBackStackEntry?.savedStateHandle?.set("profileUpdated", true)
                    nav.popBackStack()
                },
                onCancel = { nav.popBackStack() }
            )
        }
    }
}
