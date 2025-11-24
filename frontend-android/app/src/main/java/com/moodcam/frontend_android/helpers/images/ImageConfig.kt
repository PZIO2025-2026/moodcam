package com.moodcam.frontend_android.helpers.images

/** Configuration constants for image preprocessing and model inference. */

/** Centralized configuration for image preprocessing. */
object ImageDefaults {
    /** Target square size for cropped face bitmaps. */
    const val FACE_CROP_SIZE: Int = 48

    /** Divisor converting [0,255] grayscale to [0,1] range. */
    const val NORMALIZE_DIVISOR: Float = 255f

    /** Fraction of frames that log preprocessing stats. */
    const val LOG_PREPROCESS_SAMPLE_RATE: Double = 0.01 // 1% frames
    
    /** Fraction of predictions that log model output. */
    const val LOG_PREDICTION_SAMPLE_RATE: Double = 0.05 // 5% frames
}

/** Emotion classification labels and constants. */
object EmotionLabels {
    /** Ordered labels matching model output indices. */
    val LABELS: List<String> = listOf(
        "Angry", "Disgust", "Fear", "Happy", "Neutral", "Sad", "Surprise"
    )

    /** Label used when no face is detected. */
    const val NO_FACE: String = "NoFace"
}
