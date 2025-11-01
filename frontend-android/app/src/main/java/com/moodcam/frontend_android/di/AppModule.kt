package com.moodcam.frontend_android.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.EmotionHistoryRepository
import com.moodcam.frontend_android.db.UserRepository
import com.moodcam.frontend_android.viewmodel.EmotionClassifierViewModel
import com.moodcam.frontend_android.helpers.loadModelFile
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.tensorflow.lite.Interpreter

val appModule = module {
    // TensorFlow Lite Interpreter
    single {
        val context = get<Context>()
        val buffer = loadModelFile(context, "emotion_model.tflite")
        Interpreter(buffer)
    }

    // Firebase Auth
    single<FirebaseAuth> { FirebaseAuth.getInstance() }

    // Firebase Firestore
    single<FirebaseFirestore> { Firebase.firestore }

    // Repositories
    singleOf(::UserRepository)
    singleOf(::EmotionHistoryRepository)

    // ViewModels
    viewModelOf(::AuthViewModel)
    viewModelOf(::EmotionClassifierViewModel)
}