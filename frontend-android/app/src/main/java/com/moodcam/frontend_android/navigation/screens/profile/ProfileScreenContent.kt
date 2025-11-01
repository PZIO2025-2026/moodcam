package com.moodcam.frontend_android.navigation.screens.profile

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavBackStackEntry
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.UserRepository
import com.moodcam.frontend_android.ui.profile.ProfileScreen

@Composable
fun ProfileScreenContent(
    authViewModel: AuthViewModel,
    userRepository: UserRepository,
    navBackStackEntry: NavBackStackEntry,
    onEditProfile: () -> Unit
) {
    val uid = authViewModel.getUserId()
    var isProfileComplete by remember { mutableStateOf<Boolean?>(null) }
    var userName by remember { mutableStateOf("User") }
    var userAge by remember { mutableStateOf(25) }
    var userWithUsAtDays by remember { mutableStateOf("0 days") }
    var userEmail by remember { mutableStateOf("user@example.com") }

    val profileUpdated = navBackStackEntry
        .savedStateHandle
        .getLiveData<Boolean>("profileUpdated")
        .observeAsState()

    fun loadProfileData() {
        if (uid != null) {
            userRepository.checkIsProfileCompleted(uid) { isComplete ->
                if (isComplete) {
                    userRepository.getProfile(uid) { name, age, days, email ->
                        userName = name ?: "User"
                        userAge = age ?: 25
                        userWithUsAtDays = days ?: "0 days"
                        userEmail = email ?: "user@example.com"
                        isProfileComplete = true
                    }
                } else {
                    isProfileComplete = false
                }
            }
        } else {
            isProfileComplete = false
        }
    }

    LaunchedEffect(uid) {
        loadProfileData()
    }

    LaunchedEffect(profileUpdated.value) {
        if (profileUpdated.value == true) {
            loadProfileData()
            navBackStackEntry.savedStateHandle["profileUpdated"] = false
        }
    }

    ProfileScreen(
        isProfileComplete = isProfileComplete,
        userName = userName,
        userAge = userAge,
        userWithUsAtDays = userWithUsAtDays,
        userEmail = userEmail,
        onOnboardingComplete = { name, age ->
            uid?.let {
                userRepository.saveProfile(it, name, age)
            }
            userName = name
            userAge = age
            isProfileComplete = true
        },
        onEditProfileClicked = onEditProfile,
        onSignOutClicked = { authViewModel.signout() }
    )
}
