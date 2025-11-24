/** Registers the camera route (auth protected). */

package com.moodcam.frontend_android.navigation.screens.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.navigation.Routes
import com.moodcam.frontend_android.navigation.helpers.AuthorizedScreen
import com.moodcam.frontend_android.ui.camera.CameraScreen
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.cameraRoute(nav: NavHostController) {
    /**
     * Registers the camera screen route enabling real-time emotion detection.
     * Auth protected via `AuthorizedScreen`.
     *
     * @param nav Host controller used for navigation actions.
     */
    composable(Routes.CAMERA) {
        val authViewModel: AuthViewModel = koinViewModel()
        
        AuthorizedScreen(authViewModel, nav) {
            CameraScreen(
                authViewModel = authViewModel
            )
        }
    }
}
