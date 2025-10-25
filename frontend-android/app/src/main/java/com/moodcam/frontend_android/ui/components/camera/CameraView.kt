package com.moodcam.frontend_android.ui.components.camera

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

@Composable
fun CameraView(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner,
    context: Context,
    onAnalyzeImage: (ImageProxy) -> Unit,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
) {
    var cameraProvider: ProcessCameraProvider? by remember { mutableStateOf(null) }
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()
            },
            ContextCompat.getMainExecutor(context)
        )
    }

    Box(modifier = modifier) {
        if (cameraProvider != null) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )

            LaunchedEffect(cameraProvider, lensFacing) {
                val provider = cameraProvider ?: return@LaunchedEffect

                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }
                val analyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(ContextCompat.getMainExecutor(context)) { image ->
                            onAnalyzeImage(image)
                        }
                    }

                val selector = when (lensFacing) {
                    CameraSelector.LENS_FACING_FRONT -> CameraSelector.DEFAULT_FRONT_CAMERA
                    CameraSelector.LENS_FACING_BACK -> CameraSelector.DEFAULT_BACK_CAMERA
                    else -> CameraSelector.DEFAULT_BACK_CAMERA
                }

                val hasRequestedCamera = try {
                    provider.hasCamera(selector)
                } catch (exc: Exception) {
                    Log.e("CameraView", "Camera availability check failed", exc)
                    false
                }

                val cameraSelector = when {
                    hasRequestedCamera -> selector
                    provider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) -> CameraSelector.DEFAULT_BACK_CAMERA
                    provider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) -> CameraSelector.DEFAULT_FRONT_CAMERA
                    else -> null
                }

                if (cameraSelector == null) {
                    Log.e("CameraView", "No cameras available on this device.")
                    return@LaunchedEffect
                }

                try {
                    provider.unbindAll()
                    provider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        analyzer
                    )
                } catch (exc: Exception) {
                    Log.e("CameraView", "Use case binding failed", exc)
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
