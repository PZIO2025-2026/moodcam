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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.moodcam.frontend_android.ui.components.SimpleTextScreen
import com.moodcam.frontend_android.ui.home.HomeScreen
import com.moodcam.frontend_android.ui.layouts.AuthorizedScreenLayout
import com.moodcam.frontend_android.ui.profile.ProfileScreen
import com.moodcam.frontend_android.ui.history.EmotionHistoryScreen
import com.moodcam.frontend_android.ui.profile.edit.EditProfileScreen

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
                AppBottomBar(
                    currentRoute = currentRoute,
                    onNavigateClicked = { route ->
                        nav.navigate(route) {
                            popUpTo(nav.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
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
                    onUnauthorized = {
                        nav.navigate("login") {
                            popUpTo(0)
                        }
                    }
                ) {
                    HomeScreen(
                        onOpenCamera = { nav.navigate("camera") }
                    )
                }
            }
            composable("camera") {
                AuthorizedScreenLayout(
                    authViewModel = authViewModel,
                    onUnauthorized = {
                        nav.navigate("login") {
                            popUpTo(0)
                        }
                    }
                ) {
                    CameraScreen(
                        onNavigateUp = { nav.navigateUp() },
                        authViewModel = authViewModel
                    )
                }
            }
            composable("gallery") {
                AuthorizedScreenLayout(
                    authViewModel = authViewModel,
                    onUnauthorized = {
                        nav.navigate("login") {
                            popUpTo(0)
                        }
                    }
                ) {
                    SimpleTextScreen("Gallery (TODO)") // TODO
                }
            }
            composable("profile") { navBackStackEntry ->

                AuthorizedScreenLayout(
                    authViewModel = authViewModel,
                    onUnauthorized = {
                        nav.navigate("login") { popUpTo(0) }
                    }
                ) {
                    val uid = authViewModel.getUserId()
                    var isProfileComplete by remember { mutableStateOf<Boolean?>(null) }
                    var userName by remember { mutableStateOf("John Doe") }
                    var userAge by remember { mutableStateOf(25) }
                    var userWithUsAtDays by remember { mutableStateOf("30 days") }
                    var userEmail by remember { mutableStateOf("User@gmail.com") }

                    val profileUpdated = navBackStackEntry
                        .savedStateHandle
                        .getLiveData<Boolean>("profileUpdated")
                        .observeAsState()

                    val loadProfileData = {
                        if (uid != null) {
                            userRepository.checkIsProfileCompleted(uid) { isComplete ->
                                if (isComplete) {
                                    userRepository.getProfile(uid) { name, age, days, email ->
                                        userName = name ?: "User"
                                        userAge = age ?: 25
                                        isProfileComplete = true
                                    }
                                } else {
                                    isProfileComplete = false
                                }
                            }
                        } else {
                            isProfileComplete = false
                        }
                    }

                    LaunchedEffect(uid) {
                        loadProfileData()
                    }

                    LaunchedEffect(profileUpdated.value) {
                        if (profileUpdated.value == true) {
                            loadProfileData()
                            navBackStackEntry.savedStateHandle.set("profileUpdated", false)
                        }
                    }

                    ProfileScreen(
                        isProfileComplete = isProfileComplete,
                        userName = userName,
                        userAge = userAge,
                        userWithUsAtDays = userWithUsAtDays,
                        userEmail = userEmail,
                        onOnboardingComplete = { name, age ->
                            uid?.let {
                                userRepository.saveProfile(it, name, age)
                            }
                            userName = name
                            userAge = age
                            isProfileComplete = true
                        },
                        onEditProfileClicked = {
                            nav.navigate("editProfile")
                        },
                        onSignOutClicked = {
                            authViewModel.signout()
                        }
                    )
                }
            }
            composable("editProfile") {
                AuthorizedScreenLayout(
                    authViewModel = authViewModel,
                    onUnauthorized = {
                        nav.navigate("login") {
                            popUpTo(0)
                        }
                    }
                ) {
                    val uid = authViewModel.getUserId()

                    var initialName by remember { mutableStateOf("") }
                    var initialAge by remember { mutableStateOf("") }
                    var initialEmail by remember { mutableStateOf("") }
                    var isLoading by remember { mutableStateOf(true) }
                    var isSaving by remember { mutableStateOf(false) }
                    var error by remember { mutableStateOf<String?>(null) }

                    LaunchedEffect(uid) {
                        if (uid != null) {
                            userRepository.getProfile(uid) { currentName, currentAge, _, currentEmail ->
                                initialName = currentName ?: ""
                                initialAge = (currentAge ?: 18).toString()
                                initialEmail = currentEmail ?: ""
                                isLoading = false
                            }
                        } else {
                            error = "User not authenticated"
                            isLoading = false
                        }
                    }

                    EditProfileScreen(
                        initialName = initialName,
                        initialAge = initialAge,
                        initialEmail = initialEmail,
                        isLoading = isLoading,
                        isSaving = isSaving,
                        externalError = error,

                        onSaveClicked = { newName ->
                            if (uid == null) {
                                error = "User not authenticated"
                                return@EditProfileScreen
                            }

                            isSaving = true
                            error = null

                            userRepository.updateName(uid, newName)

                            nav.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("profileUpdated", true)

                            nav.popBackStack()
                        },

                        onCancelClicked = {
                            nav.popBackStack()
                        }
                    )
                }
            }
            composable("history") {
                AuthorizedScreenLayout(
                    authViewModel = authViewModel,
                    onUnauthorized = {
                        nav.navigate("login") {
                            popUpTo(0)
                        }
                    }
                ) {
                    EmotionHistoryScreen(
                        authViewModel = authViewModel
                    )
                }
            }
            composable("login") {
                LoginPage(
                    Modifier,
                    onHomeNavigate = {
                        nav.navigate("home") {
                            popUpTo(0)
                        }
                    },
                    onSignUpNavigate = {
                        nav.navigate("signup") {
                            launchSingleTop = true
                        }
                    },
                    authViewModel
                )
            }
            composable("signup") {
                SignupPage(
                    Modifier,
                    onHomeNavigate = {
                        nav.navigate("home") {
                            popUpTo(0)
                        }
                    },
                    onLoginNavigate = {
                        nav.navigate("login") {
                            popUpTo("signup") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    authViewModel
                )
            }
        }
    }
}

