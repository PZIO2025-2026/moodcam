@file:OptIn(ExperimentalPermissionsApi::class)
package com.moodcam.frontend_android.ui.camera

import android.Manifest
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.moodcam.frontend_android.ui.components.camera.CameraView
import com.moodcam.frontend_android.ui.layouts.PremiumScreenLayout
import com.moodcam.frontend_android.viewmodel.EmotionClassifierViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    navController: NavController,
    classifierViewModel: EmotionClassifierViewModel = koinViewModel()
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    when {
        cameraPermissionState.status.isGranted -> {
            Box(modifier = Modifier.fillMaxSize()) {
                var useFrontCamera by remember { mutableStateOf(false) }

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
                val emotion = classifierViewModel.currentEmotion.value
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 48.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    color = Color.Black.copy(alpha = 0.6f),
                    tonalElevation = 0.dp,
                    shadowElevation = 16.dp
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.15f),
                                        Color.White.copy(alpha = 0.05f)
                                    )
                                )
                            )
                            .padding(horizontal = 32.dp, vertical = 20.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Current Emotion",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Light,
                                letterSpacing = 1.sp
                            )
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
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Surface(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(24.dp)
                        .size(56.dp),
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.6f),
                    tonalElevation = 0.dp,
                    shadowElevation = 12.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.15f),
                                        Color.White.copy(alpha = 0.05f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Return",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                // Switch camera button (top-end)
                Surface(
                    onClick = { useFrontCamera = !useFrontCamera },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(24.dp)
                        .size(56.dp),
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.6f),
                    tonalElevation = 0.dp,
                    shadowElevation = 12.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.15f),
                                        Color.White.copy(alpha = 0.05f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Cameraswitch,
                            contentDescription = "Switch camera",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
        cameraPermissionState.status.shouldShowRationale -> {
            PremiumScreenLayout {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp)),
                    color = Color.White.copy(alpha = 0.08f),
                    tonalElevation = 0.dp,
                    shadowElevation = 24.dp
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.12f),
                                        Color.White.copy(alpha = 0.06f)
                                    )
                                )
                            )
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Camera Permission Required",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "We need camera access to analyze your emotions in real-time.",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
        else -> {
            PremiumScreenLayout {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp)),
                    color = Color.White.copy(alpha = 0.08f),
                    tonalElevation = 0.dp,
                    shadowElevation = 24.dp
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.12f),
                                        Color.White.copy(alpha = 0.06f)
                                    )
                                )
                            )
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Text(
                            "Camera Access",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "Allow camera permission to capture and analyze your mood",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { cameraPermissionState.launchPermissionRequest() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(58.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF8B5CF6),
                                            Color(0xFF6366F1)
                                        )
                                    )
                                ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 8.dp,
                                pressedElevation = 12.dp
                            )
                        ) {
                            Text(
                                "Allow Camera",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}