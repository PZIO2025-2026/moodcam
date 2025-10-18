package com.moodcam.frontend_android.viewmodel

import androidx.camera.core.ImageProxy
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.moodcam.frontend_android.helpers.images.processImageProxy
import org.tensorflow.lite.Interpreter

class EmotionClassifierViewModel(private var tflite: Interpreter): ViewModel() {
    private var frameCounter = 0
    
    private val _currentEmotion = mutableStateOf("Detecting...")
    val currentEmotion: State<String> = _currentEmotion
    
    fun predict(image: ImageProxy) {
        if (frameCounter % 10 == 0) {
            processImageProxy(image, tflite) { emotion ->
                _currentEmotion.value = emotion
            }
        } else {
            image.close()
        }
        frameCounter++
    }
}