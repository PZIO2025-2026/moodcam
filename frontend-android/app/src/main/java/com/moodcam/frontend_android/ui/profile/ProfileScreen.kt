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
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.UserRepository
import com.moodcam.frontend_android.ui.layouts.PremiumScreenLayout
import com.moodcam.frontend_android.ui.profile.onboarding.ProfileOnboardingFlow

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,

    isProfileComplete: Boolean?,
    userName: String,
    userAge: Int,
    userWithUsAtDays: String,
    userEmail: String,

    onOnboardingComplete: (name: String, age: Int) -> Unit,
    onEditProfileClicked: () -> Unit,
    onSignOutClicked: () -> Unit
) {
    if (isProfileComplete == null) {
        PremiumScreenLayout(modifier = modifier) {
            CircularProgressIndicator(
                color = Color(0xFF8B5CF6),
                modifier = Modifier.size(48.dp)
            )
        }
        return
    }

    if (!isProfileComplete) {
        ProfileOnboardingFlow(
            modifier = modifier,
            onComplete = onOnboardingComplete
        )
    } else {
        FilledProfileScreen(
            modifier = modifier,
            userName = userName,
            userAge = userAge,
            userEmail = userEmail,
            userWithUsAtDays = userWithUsAtDays,

            onEditProfileClicked = onEditProfileClicked,
            onSignOutClicked = onSignOutClicked
        )
    }
}