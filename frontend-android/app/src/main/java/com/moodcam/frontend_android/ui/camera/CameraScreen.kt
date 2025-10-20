@file:OptIn(ExperimentalPermissionsApi::class)
package com.moodcam.frontend_android.ui.camera

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController // <-- 1. Добавлен импорт
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.moodcam.frontend_android.ui.components.camera.CameraView
import com.moodcam.frontend_android.viewmodel.EmotionClassifierViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    navController: NavController,
    classifierViewModel: EmotionClassifierViewModel = koinViewModel()
){
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    when {
        cameraPermissionState.status.isGranted -> {
            Box(modifier = Modifier.fillMaxSize()) {
                // Camera preview
                CameraView(
                    modifier = Modifier.fillMaxSize(),
                    lifecycleOwner = LocalLifecycleOwner.current,
                    context = LocalContext.current,
                    onAnalyzeImage = { image ->
                        classifierViewModel.predict(image)
                    }
                )

                // Emotion display overlay
                val emotion = classifierViewModel.currentEmotion.value
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 32.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = emotion,
                        color = when (emotion) {
                            "Happy" -> Color(0xFFFFD700)
                            "Sad" -> Color(0xFF6495ED)
                            "Angry" -> Color(0xFFFF4500)
                            "Surprise" -> Color(0xFFFF69B4)
                            "Fear" -> Color(0xFF9370DB)
                            "Disgust" -> Color(0xFF32CD32)
                            "Neutral" -> Color.White
                            "NoFace" -> Color.Gray
                            else -> Color.White
                        },
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Return",
                        tint = Color.White
                    )
                }
            }
        }
        cameraPermissionState.status.shouldShowRationale -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Camera permission is required to use this feature.")
            }
        }
        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Request camera permission")
                }
            }
        }
    }
}