package com.moodcam.frontend_android.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.moodcam.frontend_android.ui.layouts.PremiumScreenLayout

@Composable
fun HomeScreen(
	modifier: Modifier = Modifier,
	onOpenCamera: () -> Unit
) {
	PremiumScreenLayout(modifier = modifier) {
		Text(
			text = "MOODCAM",
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

		Spacer(modifier = Modifier.height(12.dp))

		Text(
			text = "Welcome back!",
			fontSize = 18.sp,
			color = Color.White.copy(alpha = 0.7f),
			fontWeight = FontWeight.Light
		)

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
					.padding(28.dp),
				verticalArrangement = Arrangement.spacedBy(20.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = "Capture your mood",
					fontSize = 20.sp,
					fontWeight = FontWeight.Medium,
					color = Color.White
				)

				Text(
					text = "Take a photo and analyze your emotions in real-time",
					fontSize = 14.sp,
					color = Color.White.copy(alpha = 0.7f),
					fontWeight = FontWeight.Light
				)

				Spacer(modifier = Modifier.height(16.dp))

				Button(
					onClick = onOpenCamera,
					modifier = Modifier
						.fillMaxWidth()
						.height(58.dp)
						.clip(RoundedCornerShape(16.dp))
						.background(
							brush = Brush.horizontalGradient(
								colors = listOf(
									Color(0xFF8B5CF6),
									Color(0xFF6366F1)
								)
							)
						),
					colors = ButtonDefaults.buttonColors(
						containerColor = Color.Transparent
					),
					elevation = ButtonDefaults.buttonElevation(
						defaultElevation = 8.dp,
						pressedElevation = 12.dp
					)
				) {
					Icon(
						imageVector = Icons.Filled.CameraAlt,
						contentDescription = "Camera",
						tint = Color.White,
						modifier = Modifier.size(24.dp)
					)
					Spacer(modifier = Modifier.width(12.dp))
					Text(
						"Open Camera",
						fontSize = 18.sp,
						fontWeight = FontWeight.SemiBold,
						color = Color.White
					)
				}
			}
		}
	}
}