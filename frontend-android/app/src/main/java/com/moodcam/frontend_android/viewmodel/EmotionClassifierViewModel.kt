package com.moodcam.frontend_android.viewmodel

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import com.moodcam.frontend_android.helpers.images.processImageWithModel
import org.tensorflow.lite.Interpreter

class EmotionClassifier(private var tflite: Interpreter): ViewModel() {
    fun predict(image:ImageProxy,onEmotionDetected: (String) -> Unit): String {
        val output = Array(1) { FloatArray(7) }
        processImageWithModel(image, tflite, onEmotionDetected)

        val predictedClass = output[0].indices.maxBy { output[0][it] }
        return "Emotion: $predictedClass"
    }
}