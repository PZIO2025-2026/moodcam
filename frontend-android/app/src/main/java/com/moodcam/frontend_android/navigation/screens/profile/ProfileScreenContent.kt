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
    onEditProfile: () -> Unit
) {
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
                onSignOutClicked = {}
            )
        }
        is ProfileState.Loaded -> {
            ProfileScreen(
                isProfileComplete = state.isComplete,
                userName = state.name,
                userAge = state.age,
                userWithUsAtDays = state.daysWithUs,
                userEmail = state.email,
                onOnboardingComplete = { name, age ->
                    profileViewModel.saveProfile(name, age)
                },
                onEditProfileClicked = onEditProfile,
                onSignOutClicked = { authViewModel.signout() }
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
