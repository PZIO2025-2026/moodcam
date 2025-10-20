@file:OptIn(ExperimentalMaterial3Api::class)

package com.moodcam.frontend_android.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.ui.layouts.AuthorizedScreenLayout

@Composable
fun HomeScreen(
	modifier: Modifier = Modifier,
	navController: NavController,
	authViewModel: AuthViewModel,

	onOpenCamera: () -> Unit
) {

	AuthorizedScreenLayout(authViewModel = authViewModel, navController = navController) {
		Column(
			modifier = modifier
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
		}
	}
}