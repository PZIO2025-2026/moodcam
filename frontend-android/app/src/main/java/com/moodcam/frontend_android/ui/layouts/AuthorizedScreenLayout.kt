package com.moodcam.frontend_android.ui.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import com.moodcam.frontend_android.auth.vm.AuthState
import com.moodcam.frontend_android.auth.vm.AuthViewModel

@Composable
fun AuthorizedScreenLayout(
    authViewModel: AuthViewModel,
    onUnauthorized: () -> Unit,
    content: @Composable () -> Unit
) {
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                onUnauthorized()
            }
            else -> Unit
        }
    }

    if (authState.value !is AuthState.Unauthenticated) {
        content()
    }
}