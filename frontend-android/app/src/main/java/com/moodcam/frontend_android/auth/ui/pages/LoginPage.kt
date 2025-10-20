package com.moodcam.frontend_android.auth.ui.pages
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.moodcam.frontend_android.auth.ui.elements.AuthScreenLayout
import com.moodcam.frontend_android.auth.ui.elements.EmailAuthField
import com.moodcam.frontend_android.auth.ui.elements.PasswordAuthField
import com.moodcam.frontend_android.auth.vm.AuthState
import com.moodcam.frontend_android.auth.vm.AuthViewModel

@Composable
fun LoginPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated ->  navController.navigate("home"){
                popUpTo(0)
            }
            is AuthState.Error -> Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    AuthScreenLayout(modifier = modifier) {

        EmailAuthField(
            value = email,
            onValueChange = { email = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordAuthField(
            value = password,
            onValueChange = { password = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            authViewModel.login(email, password)
        }) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(24.dp))


        TextButton(onClick = {
            navController.navigate("signup"){
                launchSingleTop = true
            }
        }) {
            Text(text = "Don't have account? Signup!")
        }
    }
}