package com.moodcam.frontend_android.navigation.screens.profile

import androidx.compose.runtime.*
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.UserRepository
import com.moodcam.frontend_android.ui.profile.edit.EditProfileScreen

@Composable
fun EditProfileScreenContent(
    authViewModel: AuthViewModel,
    userRepository: UserRepository,
    onSaveComplete: () -> Unit,
    onCancel: () -> Unit
) {
    val uid = authViewModel.getUserId()
    var initialName by remember { mutableStateOf("") }
    var initialAge by remember { mutableStateOf("") }
    var initialEmail by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uid) {
        if (uid != null) {
            userRepository.getProfile(uid) { currentName, currentAge, _, currentEmail ->
                initialName = currentName ?: ""
                initialAge = (currentAge ?: 18).toString()
                initialEmail = currentEmail ?: ""
                isLoading = false
            }
        } else {
            error = "User not authenticated"
            isLoading = false
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
            userRepository.updateName(uid, newName)
            onSaveComplete()
        },
        onCancelClicked = onCancel
    )
}
