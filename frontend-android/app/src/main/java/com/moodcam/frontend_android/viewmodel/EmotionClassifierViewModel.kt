/** Real-time emotion classification ViewModel wrapping a TensorFlow Lite interpreter.
 * Exposes current emotion state and skips frames (processes every 10th) for performance.
 */
package com.moodcam.frontend_android.viewmodel

import androidx.camera.core.ImageProxy
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.moodcam.frontend_android.helpers.images.processImageProxy
import org.tensorflow.lite.Interpreter

/**
 * @property tflite TensorFlow Lite interpreter with loaded emotion model.
 */
class EmotionClassifierViewModel(private var tflite: Interpreter): ViewModel() {
    private var frameCounter = 0
    
    private val _currentEmotion = mutableStateOf("Detecting...")
    val currentEmotion: State<String> = _currentEmotion
    
    /** Processes every 10th frame updating `currentEmotion`; closes skipped frames.
     * @param image CameraX frame proxy.
     */
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