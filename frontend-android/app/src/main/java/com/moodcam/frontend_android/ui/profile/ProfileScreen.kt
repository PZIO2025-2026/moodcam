package com.moodcam.frontend_android.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.UserRepository
import com.moodcam.frontend_android.ui.layouts.PremiumScreenLayout
import com.moodcam.frontend_android.ui.profile.onboarding.ProfileOnboardingFlow

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    userRepository: UserRepository
) {

    var uid = authViewModel.getUserId();


    var isProfileComplete by remember { mutableStateOf<Boolean?>(null) }
    var userName by remember { mutableStateOf("John Doe") }
    var userAge by remember { mutableStateOf(25) }
    var userWithUsAtDays by remember { mutableStateOf("30 days") }
    var userEmail by remember { mutableStateOf("User@gmail.com") }

    LaunchedEffect(uid) {
        if (uid != null) {
            userRepository.checkIsProfileCompleted(uid) { isComplete ->
                if (isComplete) {
                    userRepository.getProfile(uid) { name, age, days, email ->
                        userName = name ?: "User"
                        userAge = age ?: 25
                        userWithUsAtDays = days ?: "30 days"
                        userEmail = email ?: "User@gmail.com"
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
    if (isProfileComplete == null) {
        PremiumScreenLayout(modifier = modifier) {
            CircularProgressIndicator(
                color = Color(0xFF8B5CF6),
                modifier = Modifier.size(48.dp)
            )
        }
        return
    }

    if (!isProfileComplete!!) {
        ProfileOnboardingFlow(
            modifier = modifier,
            onComplete = { name, age ->
                userName = name
                userAge = age
                isProfileComplete = true
                uid?.let {
                    userRepository.saveProfile(it, name, age)
                }
            }
        )
    } else {
        FilledProfileScreen(
            modifier = modifier,
            userName = userName,
            userAge = userAge,
            userEmail = userEmail,
            userWithUsAtDays = userWithUsAtDays,
            authViewModel = authViewModel
        )
    }
}

@Composable
private fun FilledProfileScreen(
    modifier: Modifier = Modifier,
    userName: String,
    userAge: Int,
    userWithUsAtDays: String,
    userEmail: String,
    authViewModel: AuthViewModel
) {
    PremiumScreenLayout(modifier = modifier) {
        Text(
            text = "PROFILE",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            style = LocalTextStyle.current.copy(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFBFBFB),
                        Color(0xFFE0E0E0)
                    )
                )
            ),
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Surface(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            color = Color.White.copy(alpha = 0.1f),
            tonalElevation = 0.dp,
            shadowElevation = 16.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF8B5CF6).copy(alpha = 0.3f),
                                Color(0xFF6366F1).copy(alpha = 0.2f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile Avatar",
                    modifier = Modifier.size(80.dp),
                    tint = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp)),
            color = Color.White.copy(alpha = 0.08f),
            tonalElevation = 0.dp,
            shadowElevation = 24.dp
        ) {
            Column(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.12f),
                                Color.White.copy(alpha = 0.06f)
                            )
                        )
                    )
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Name: $userName",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = { /* TODO: Edit name */ },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit Name",
                            tint = Color(0xFF8B5CF6),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.White.copy(alpha = 0.2f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Email: $userEmail",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = { /* TODO: Edit email */ },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit Email",
                            tint = Color(0xFF8B5CF6),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.White.copy(alpha = 0.2f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your age: $userAge",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.White.copy(alpha = 0.2f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "You with us: $userWithUsAtDays",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { authViewModel.signout() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White.copy(alpha = 0.8f)
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Color.White.copy(alpha = 0.3f)
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Logout,
                contentDescription = "Logout",
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Sign Out",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
