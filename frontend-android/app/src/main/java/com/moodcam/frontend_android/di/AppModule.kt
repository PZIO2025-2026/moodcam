package com.moodcam.frontend_android.di

import android.content.Context
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.moodcam.frontend_android.viewmodel.EmotionClassifierViewModel
import com.moodcam.frontend_android.helpers.loadModelFile
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.tensorflow.lite.Interpreter

val appModule = module {
    single {
        val context: Context = get()
        val buffer = loadModelFile(context,"emotion_model.tflite")
        Interpreter(buffer)
    }
    viewModel { EmotionClassifierViewModel(get()) }
}