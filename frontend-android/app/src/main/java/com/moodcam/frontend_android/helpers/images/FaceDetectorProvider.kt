package com.moodcam.frontend_android.helpers.images

import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions

/**
 * Provides a singleton instance of ML Kit FaceDetector with configured options.
 * Helps avoid per-frame instantiation and centralizes configuration.
 */
object FaceDetectorProvider {
    private val options: FaceDetectorOptions by lazy {
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .build()
    }

    val detector: FaceDetector by lazy { FaceDetection.getClient(options) }
}
