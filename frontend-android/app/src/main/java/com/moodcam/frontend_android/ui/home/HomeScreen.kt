package com.moodcam.frontend_android.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.ui.components.stats.EmotionStatsCard
import com.moodcam.frontend_android.ui.components.stats.RecentEmotionsCard
import com.moodcam.frontend_android.ui.layouts.PremiumScreenLayout
import org.koin.androidx.compose.koinViewModel

/** Home dashboard displaying branding, stats and recent emotion detections.
 * First authenticated landing screen.
 * @param modifier optional root modifier.
 * @param onOpenCamera navigate to camera callback.
 * @param authViewModel auth view model.
 */
@Composable
fun HomeScreen(
	modifier: Modifier = Modifier,
	onOpenCamera: () -> Unit,
	authViewModel: AuthViewModel = koinViewModel()
) {
	val userId = authViewModel.getUserId()
	val scrollState = rememberScrollState()
	
	PremiumScreenLayout(modifier = modifier) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.verticalScroll(scrollState),
			verticalArrangement = Arrangement.spacedBy(24.dp)
		) {
			// Header
			Column {
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

				Spacer(modifier = Modifier.height(8.dp))

				Text(
					text = "Track your emotions",
					fontSize = 16.sp,
					color = Color.White.copy(alpha = 0.7f),
					fontWeight = FontWeight.Light
				)
			}
			// Emotion Statistics Card
			EmotionStatsCard(
				userId = userId,
				modifier = Modifier.fillMaxWidth()
			)

			// Recent Activity Card
			RecentEmotionsCard(
				userId = userId,
				limit = 8,
				modifier = Modifier.fillMaxWidth()
			)
			
			// Bottom padding for scroll
			Spacer(modifier = Modifier.height(16.dp))
		}
	}
}