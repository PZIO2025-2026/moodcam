package com.moodcam.frontend_android.helpers.images

/** Centralized defaults for image preprocessing and logging. */
object ImageDefaults {
    // Target square size for face crops expected by the model
    const val FACE_CROP_SIZE: Int = 48

    // Grayscale normalization divisor (to scale 0..255 -> 0..1)
    const val NORMALIZE_DIVISOR: Float = 255f

    // Sampling rates to reduce log spam
    const val LOG_PREPROCESS_SAMPLE_RATE: Double = 0.01 // 1% frames
    const val LOG_PREDICTION_SAMPLE_RATE: Double = 0.05 // 5% frames
}

/** Labels and constants for emotion classification output. */
object EmotionLabels {
    // Order must match the TFLite model output
    val LABELS: List<String> = listOf(
        "Angry", "Disgust", "Fear", "Happy", "Neutral", "Sad", "Surprise"
    )

    const val NO_FACE: String = "NoFace"
}
