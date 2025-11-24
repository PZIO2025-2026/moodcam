/** Layout enforcing authentication before rendering protected content. */

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
    /**
     * Layout wrapper enforcing authentication state before rendering content.
     * Navigates via `onUnauthorized` when user becomes unauthenticated.
     *
     * @param authViewModel Authentication ViewModel.
     * @param onUnauthorized Callback invoked when state transitions to unauthenticated.
     * @param content Composable content shown only when authenticated.
     */
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