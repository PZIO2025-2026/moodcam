package com.moodcam.frontend_android.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.moodcam.frontend_android.auth.ui.pages.LoginPage
import com.moodcam.frontend_android.auth.ui.pages.SignupPage
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.UserRepository
import com.moodcam.frontend_android.ui.camera.CameraScreen
import com.moodcam.frontend_android.ui.components.BottomNavItem
import com.moodcam.frontend_android.ui.home.HomeScreen
import com.moodcam.frontend_android.ui.layouts.AuthorizedScreenLayout
import com.moodcam.frontend_android.ui.profile.ProfileScreen

// bottom bar
val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Default.Home, "home"),
    BottomNavItem("History", Icons.Default.List, "history"),
    BottomNavItem("Gallery", Icons.Default.PhotoLibrary, "gallery"),
    BottomNavItem("Profile", Icons.Default.AccountCircle, "profile")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    userRepository: UserRepository
) {
    val nav = rememberNavController()

    val navBackStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Screens without topbar and bottom bar
    val fullScreenRoutes = listOf("login", "signup", "camera")
    val shouldShowBars = currentRoute != null && currentRoute !in fullScreenRoutes

    Scaffold(
        modifier = modifier,
        containerColor = Color(0xFF0F0C29),
        topBar = {
            if (shouldShowBars) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "MoodCam",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFF1A1625)
                    )
                )
            }
        },
        bottomBar = {
            if (shouldShowBars) {
                AppBottomBar(navController = nav, currentRoute = currentRoute)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = nav,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                AuthorizedScreenLayout(
                    authViewModel = authViewModel,
                    navController = nav
                ) {
                    HomeScreen(
                        navController = nav,
                        authViewModel = authViewModel,
                        onOpenCamera = { nav.navigate("camera") }
                    )
                }
            }
            composable("camera") {
                AuthorizedScreenLayout(
                    authViewModel = authViewModel,
                    navController = nav
                ) {
                    CameraScreen(navController = nav)
                }
            }
            composable("gallery") {
                AuthorizedScreenLayout(
                    authViewModel = authViewModel,
                    navController = nav
                ) {
                    SimpleTextScreen("Gallery (TODO)") // TODO
                }
            }
            composable("profile") {
                AuthorizedScreenLayout(
                    authViewModel = authViewModel,
                    navController = nav
                ) {
                    ProfileScreen(
                        navController = nav,
                        authViewModel = authViewModel,
                        userRepository = userRepository
                    )
                }
            }
            composable("history") {
                AuthorizedScreenLayout(
                    authViewModel = authViewModel,
                    navController = nav
                ) {
                    SimpleTextScreen("History (TODO)") // TODO
                }
            }
            composable("login") {
                LoginPage(Modifier, nav, authViewModel)
            }
            composable("signup") {
                SignupPage(Modifier, nav, authViewModel)
            }
        }
    }
}

@Composable
fun AppBottomBar(navController: NavController, currentRoute: String?) {
    NavigationBar(
        containerColor = Color(0xFF1A1625),
        contentColor = Color.White
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentRoute?.let {
                item.route == it
            } ?: false

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected)
                            Color(0xFF8B5CF6)
                        else
                            Color.White.copy(alpha = 0.6f)
                    )
                },
                label = {
                    Text(
                        item.label,
                        color = if (isSelected)
                            Color(0xFF8B5CF6)
                        else
                            Color.White.copy(alpha = 0.6f)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF8B5CF6),
                    selectedTextColor = Color(0xFF8B5CF6),
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color(0xFF8B5CF6).copy(alpha = 0.2f)
                )
            )
        }
    }
}

@Composable
private fun SimpleTextScreen(text: String) {
    Text(text)
}