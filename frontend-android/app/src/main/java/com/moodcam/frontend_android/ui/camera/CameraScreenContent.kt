package com.moodcam.frontend_android.ui.camera

import androidx.camera.core.CameraSelector
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.EmotionHistoryRepository
import com.moodcam.frontend_android.ui.components.camera.*
import com.moodcam.frontend_android.viewmodel.EmotionClassifierViewModel

/**
 * Full camera screen with live emotion detection overlay, save action, camera switch and feedback.
 * @param authViewModel Authentication view model.
 * @param classifierViewModel Emotion classifier view model.
 * @param historyRepository Repository for persisting emotions.
 */
@Composable
fun CameraScreenContent(
    authViewModel: AuthViewModel,
    classifierViewModel: EmotionClassifierViewModel,
    historyRepository: EmotionHistoryRepository
) {
    var useFrontCamera by remember { mutableStateOf(false) }
    var showSavedMessage by remember { mutableStateOf(false) }
    val emotion = classifierViewModel.currentEmotion.value

    Box(modifier = Modifier.fillMaxSize()) {
        // Camera preview (full screen)
        CameraView(
            modifier = Modifier.fillMaxSize(),
            lifecycleOwner = LocalLifecycleOwner.current,
            context = LocalContext.current,
            onAnalyzeImage = { image ->
                classifierViewModel.predict(image)
            },
            cameraSelector = if (useFrontCamera) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
        )

        // Emotion display card (top-center)
        EmotionDisplayCard(
            emotion = emotion,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp)
        )

        // Save button (bottom-center)
        SaveEmotionButton(
            onClick = {
                val uid = authViewModel.getUserId()
                if (uid != null && emotion !in listOf("Detecting...", "NoFace")) {
                    historyRepository.addEmotion(uid, emotion)
                    showSavedMessage = true
                }
            },
            isEnabled = emotion !in listOf("Detecting...", "NoFace"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        )

        // Saved message snackbar
        if (showSavedMessage) {
            SavedEmotionMessage(
                onDismiss = { showSavedMessage = false },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 128.dp)
            )
        }

        // Switch camera button (top-end)
        SwitchCameraButton(
            onClick = { useFrontCamera = !useFrontCamera },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
        )
    }
}
