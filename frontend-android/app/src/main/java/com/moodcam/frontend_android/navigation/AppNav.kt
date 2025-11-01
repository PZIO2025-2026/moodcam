package com.moodcam.frontend_android.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.moodcam.frontend_android.navigation.components.AppBottomBar
import com.moodcam.frontend_android.navigation.components.AppTopBar
import com.moodcam.frontend_android.navigation.helpers.navigateToBottomNavDestination
import com.moodcam.frontend_android.navigation.screens.auth.loginRoute
import com.moodcam.frontend_android.navigation.screens.auth.signupRoute
import com.moodcam.frontend_android.navigation.screens.main.*
import com.moodcam.frontend_android.navigation.screens.profile.editProfileRoute
import com.moodcam.frontend_android.navigation.screens.profile.profileRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav(
    modifier: Modifier = Modifier
) {
    val nav = rememberNavController()
    val navBackStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldShowBars = currentRoute !in Routes.FULL_SCREEN_ROUTES

    Scaffold(
        modifier = modifier,
        containerColor = Color(0xFF0F0C29),
        topBar = { if (shouldShowBars) AppTopBar() },
        bottomBar = { 
            if (shouldShowBars) {
                AppBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = nav::navigateToBottomNavDestination
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = nav,
            startDestination = Routes.LOGIN,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Auth routes
            loginRoute(nav)
            signupRoute(nav)
            
            // Main routes
            homeRoute(nav)
            cameraRoute(nav)
            galleryRoute(nav)
            historyRoute(nav)
            
            // Profile routes
            profileRoute(nav)
            editProfileRoute(nav)
        }
    }
}
