package com.moodcam.frontend_android.navigation

/** Centralized navigation route definitions used throughout the app. */
object Routes {
    /** User login screen route. */
    const val LOGIN = "login"
    
    /** User registration screen route. */
    const val SIGNUP = "signup"
    
    /** Home/dashboard screen route. */
    const val HOME = "home"
    
    /** Camera emotion detection screen route. */
    const val CAMERA = "camera"
    
    /** User profile screen route. */
    const val PROFILE = "profile"
    
    /** Profile editing screen route. */
    const val EDIT_PROFILE = "editProfile"
    
    /** Settings screen route. */
    const val SETTINGS = "settings"
    
    /** Emotion history screen route. */
    const val HISTORY = "history"
    
    /** Statistics and analytics screen route. */
    const val STATISTICS = "statistics"
    
    /** Routes that hide top and bottom bars for full-screen experience. */
    val FULL_SCREEN_ROUTES = listOf(LOGIN, SIGNUP)
}
