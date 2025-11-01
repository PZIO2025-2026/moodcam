package com.moodcam.frontend_android.navigation.helpers

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.moodcam.frontend_android.navigation.Routes

fun NavHostController.navigateToBottomNavDestination(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavHostController.navigateToLogin() {
    navigate(Routes.LOGIN) {
        popUpTo(0)
    }
}

fun NavHostController.navigateToHome() {
    navigate(Routes.HOME) {
        popUpTo(0)
    }
}
