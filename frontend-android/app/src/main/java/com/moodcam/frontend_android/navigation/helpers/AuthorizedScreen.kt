package com.moodcam.frontend_android.navigation.helpers

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.ui.layouts.AuthorizedScreenLayout

@Composable
fun AuthorizedScreen(
    authViewModel: AuthViewModel,
    nav: NavHostController,
    content: @Composable () -> Unit
) {
    AuthorizedScreenLayout(
        authViewModel = authViewModel,
        onUnauthorized = { nav.navigateToLogin() },
        content = content
    )
}
