package com.moodcam.frontend_android.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.EmotionHistoryRepository
import com.moodcam.frontend_android.db.UserRepository
import com.moodcam.frontend_android.db.EmotionHistoryRepository
import com.moodcam.frontend_android.viewmodel.EmotionClassifierViewModel
import com.moodcam.frontend_android.helpers.loadModelFile
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.dsl.single
import org.tensorflow.lite.Interpreter

val appModule = module {
    single {
        val context: Context = get()
        val buffer = loadModelFile(context,"emotion_model.tflite")
        Interpreter(buffer)
    }
    // Firebase Auth
    single { FirebaseAuth.getInstance() }

    // Firebase db
    single { Firebase.firestore }

    //repository
    single { UserRepository(get()) }
    single { EmotionHistoryRepository(get())}

    //viewModels
    viewModel { AuthViewModel(get(), get()) }
    viewModel { EmotionClassifierViewModel(get()) }
}