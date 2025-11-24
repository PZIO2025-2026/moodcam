/** Main camera screen handling permission flow and showing detection UI. */

@file:OptIn(ExperimentalPermissionsApi::class)
package com.moodcam.frontend_android.ui.camera

import android.Manifest
import androidx.compose.runtime.Composable
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.EmotionHistoryRepository
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.moodcam.frontend_android.ui.components.camera.CameraPermissionRationale
import com.moodcam.frontend_android.ui.components.camera.CameraPermissionRequest
import com.moodcam.frontend_android.viewmodel.EmotionClassifierViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Chooses between camera content, rationale or permission request based on state.
 * @param authViewModel Auth view model.
 * @param classifierViewModel Emotion classifier view model.
 * @param historyRepository Repository for persisting emotions.
 */
@Composable
fun CameraScreen(
    authViewModel: AuthViewModel,
    classifierViewModel: EmotionClassifierViewModel = koinViewModel(),
    historyRepository: EmotionHistoryRepository = org.koin.androidx.compose.get()
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    when {
        cameraPermissionState.status.isGranted -> {
            CameraScreenContent(
                authViewModel = authViewModel,
                classifierViewModel = classifierViewModel,
                historyRepository = historyRepository
            )
        }
        cameraPermissionState.status.shouldShowRationale -> {
            CameraPermissionRationale()
        }
        else -> {
            CameraPermissionRequest(
                onRequestPermission = { cameraPermissionState.launchPermissionRequest() }
            )
        }
    }
}

