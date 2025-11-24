package com.moodcam.frontend_android.helpers.images

import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions

/** Singleton provider for an ML Kit `FaceDetector`. */

/** Centralized FaceDetector configuration (FAST performance mode). */
object FaceDetectorProvider {
    /** Face detector options (FAST performance mode). */
    private val options: FaceDetectorOptions by lazy {
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .build()
    }

    /** Lazy singleton detector instance. */
    val detector: FaceDetector by lazy { FaceDetection.getClient(options) }
}
