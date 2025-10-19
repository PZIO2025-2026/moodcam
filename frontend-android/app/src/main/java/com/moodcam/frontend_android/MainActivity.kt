package com.moodcam.frontend_android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.di.appModule
import com.moodcam.frontend_android.navigation.AppNav
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.startKoin
import org.opencv.android.OpenCVLoader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (OpenCVLoader.initLocal()) {
            Log.i("OpenCV", "OpenCV loaded successfully!")
        } else {
            Log.e("OpenCV", "OpenCV was not loaded!")
        }
        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(appModule)
        }
        setContent {
            //UI components here
            MaterialTheme {
                val authViewModel: AuthViewModel = koinViewModel()

                AppNav(authViewModel = authViewModel)
            }
        }
    }
}