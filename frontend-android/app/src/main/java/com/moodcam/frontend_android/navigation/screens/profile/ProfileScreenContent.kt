/** Loads, observes and forwards profile state to `ProfileScreen`. */

package com.moodcam.frontend_android.navigation.screens.profile

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavBackStackEntry
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.ui.profile.ProfileScreen
import com.moodcam.frontend_android.viewmodel.ProfileState
import com.moodcam.frontend_android.viewmodel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreenContent(
    navBackStackEntry: NavBackStackEntry,
    onEditProfile: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    /**
     * Composable wrapper that loads and observes profile state, forwarding
     * derived values to `ProfileScreen` and handling refresh triggers from edit screen.
     *
     * @param navBackStackEntry Back stack entry used to observe saved state handle flags.
     * @param onEditProfile Callback when user chooses to edit profile.
     * @param onSettingsClicked Callback when settings icon is pressed.
     */
    val authViewModel: AuthViewModel = koinViewModel()
    val profileViewModel: ProfileViewModel = koinViewModel()
    val profileState by profileViewModel.profileState.collectAsState()

    val profileUpdated = navBackStackEntry
        .savedStateHandle
        .getLiveData<Boolean>("profileUpdated")
        .observeAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    LaunchedEffect(profileUpdated.value) {
        if (profileUpdated.value == true) {
            profileViewModel.loadProfile()
            navBackStackEntry.savedStateHandle.set("profileUpdated", false)
        }
    }

    when (val state = profileState) {
        is ProfileState.Loading -> {
            ProfileScreen(
                isProfileComplete = null,
                userName = "Loading...",
                userAge = 0,
                userWithUsAtDays = "...",
                userEmail = "...",
                onOnboardingComplete = { _, _ -> },
                onEditProfileClicked = {},
                onSignOutClicked = {},
                onSettingsClicked = {}
            )
        }
        is ProfileState.Loaded -> {
            ProfileScreen(
                isProfileComplete = state.user.isProfileComplete(),
                userName = state.user.name ?: "User",
                userAge = state.user.getCurrentAge() ?: state.user.userStartAge ?: 25,
                userWithUsAtDays = state.user.getDaysWithUs(),
                userEmail = state.user.email,
                onOnboardingComplete = { name, age ->
                    profileViewModel.saveProfile(name, age)
                },
                onEditProfileClicked = onEditProfile,
                onSignOutClicked = { authViewModel.signout() },
                onSettingsClicked = onSettingsClicked
            )
        }
        is ProfileState.Unauthenticated -> {
            // Handled by AuthorizedScreen
        }
        is ProfileState.Error -> {
            // TODO: Show error UI
        }
    }
}
