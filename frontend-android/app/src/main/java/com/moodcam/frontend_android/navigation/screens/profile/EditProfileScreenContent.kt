package com.moodcam.frontend_android.navigation.screens.profile

import androidx.compose.runtime.*
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.ui.profile.edit.EditProfileScreen
import com.moodcam.frontend_android.viewmodel.ProfileViewModel
import com.moodcam.frontend_android.viewmodel.ProfileState
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditProfileScreenContent(
    onSaveComplete: () -> Unit,
    onCancel: () -> Unit
) {
    val authViewModel: AuthViewModel = koinViewModel()
    val profileViewModel: ProfileViewModel = koinViewModel()
    
    val uid = authViewModel.getUserId()
    var initialName by remember { mutableStateOf("") }
    var initialAge by remember { mutableStateOf("") }
    var initialEmail by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val profileState by profileViewModel.profileState.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    LaunchedEffect(profileState) {
        when (val state = profileState) {
            is ProfileState.Loaded -> {
                initialName = state.user.name ?: ""
                initialAge = (state.user.getCurrentAge() ?: state.user.userStartAge ?: 25).toString()
                initialEmail = state.user.email
                isLoading = false
            }
            is ProfileState.Error -> {
                error = state.message
                isLoading = false
            }
            else -> {}
        }
    }

    EditProfileScreen(
        initialName = initialName,
        initialAge = initialAge,
        initialEmail = initialEmail,
        isLoading = isLoading,
        isSaving = isSaving,
        externalError = error,
        onSaveClicked = { newName ->
            if (uid == null) {
                error = "User not authenticated"
                return@EditProfileScreen
            }
            isSaving = true
            error = null
            profileViewModel.updateName(newName)
            isSaving = false
            onSaveComplete()
        },
        onCancelClicked = onCancel
    )
}
