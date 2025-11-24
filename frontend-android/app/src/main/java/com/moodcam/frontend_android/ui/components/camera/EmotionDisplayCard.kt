/** Displays the current detected emotion in a stylized card. */

package com.moodcam.frontend_android.ui.components.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Card showing the current emotion with color-coded styling.
 * @param emotion Detected emotion label.
 * @param modifier Optional modifier.
 */
@Composable
fun EmotionDisplayCard(
    emotion: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(24.dp)),
        color = Color.Black.copy(alpha = 0.6f),
        tonalElevation = 0.dp,
        shadowElevation = 16.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.15f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(horizontal = 32.dp, vertical = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Current Emotion",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Light,
                    letterSpacing = 1.sp
                )
                Text(
                    text = emotion,
                    color = getEmotionColor(emotion),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/** Returns a display color for an emotion label. */
private fun getEmotionColor(emotion: String): Color {
    return when (emotion) {
        "Happy" -> Color(0xFFFFD700)
        "Sad" -> Color(0xFF6495ED)
        "Angry" -> Color(0xFFFF4500)
        "Surprise" -> Color(0xFFFF69B4)
        "Fear" -> Color(0xFF9370DB)
        "Disgust" -> Color(0xFF32CD32)
        "Neutral" -> Color.White
        "NoFace" -> Color.Gray
        else -> Color.White
    }
}
