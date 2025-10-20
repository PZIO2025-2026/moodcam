@file:OptIn(ExperimentalMaterial3Api::class)

package com.moodcam.frontend_android.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.moodcam.frontend_android.auth.vm.AuthState
import com.moodcam.frontend_android.auth.vm.AuthViewModel

@Composable
fun HomeScreen(
	modifier: Modifier = Modifier,
	navController: NavController,
	authViewModel: AuthViewModel,

	onOpenCamera: () -> Unit,
	onOpenGallery: () -> Unit,
	onOpenHistory: () -> Unit = {},
) {
	val authState = authViewModel.authState.observeAsState()

	LaunchedEffect(authState.value) {
		when (authState.value) {
			is AuthState.Unauthenticated ->  navController.navigate("login"){
				popUpTo(0)
			}
			else -> Unit
		}
	}

	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(title = { Text("MoodCam") })
		}
	) { inner ->
		Column(
			modifier = Modifier
				.padding(inner)
				.fillMaxSize()
				.padding(24.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
		) {
			Text(
				text = "Witaj!",
				style = MaterialTheme.typography.headlineMedium
			)
			Text(
				text = "Rób zdjęcia i analizuj nastrój.",
				style = MaterialTheme.typography.bodyLarge
			)

			TextButton(onClick = {
				authViewModel.signout()
			}) {
				Text(text = "Sign out")
			}

			Spacer(Modifier.height(8.dp))

			Button(onClick = onOpenCamera, modifier = Modifier.fillMaxWidth()) {
				Text("Otwórz camere")
			}
			OutlinedButton(onClick = onOpenGallery, modifier = Modifier.fillMaxWidth()) {
				Text("Otwórz galerię")
			}
			OutlinedButton(onClick = onOpenHistory, modifier = Modifier.fillMaxWidth()) {
				Text("Historia analiz")
			}
		}
	}
}

//@Preview(showBackground = true)
//@Composable
//private fun HomeScreenPreview() {
//	HomeScreen(
//
//
//		onOpenCamera = {},
//		onOpenGallery = {},
//		onOpenHistory = {},
//	)
//}