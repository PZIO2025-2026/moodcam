package com.moodcam.frontend_android.ui.components.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moodcam.frontend_android.db.EmotionHistoryRepository
import com.moodcam.frontend_android.ui.components.charts.EmotionPieChart
import org.koin.compose.koinInject

/**
 * Card component showing emotion statistics with Pie Chart
 */
@Composable
fun EmotionStatsCard(
    userId: String?,
    modifier: Modifier = Modifier,
    historyRepository: EmotionHistoryRepository = koinInject()
) {
    var emotionCounts by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    var isLoading by remember { mutableStateOf(true) }

    // Load recent emotions
    LaunchedEffect(userId) {
        if (userId != null) {
            historyRepository.getRecent(userId, null, 30) { emotions ->
                emotionCounts = emotions.groupingBy { it.emotion }.eachCount()
                isLoading = false
            }
        } else {
            isLoading = false
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        color = Color.White.copy(alpha = 0.08f),
        tonalElevation = 0.dp,
        shadowElevation = 16.dp
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
                .padding(24.dp)
        ) {
            Text(
                text = "Your Mood Stats",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = "Last 30 detections",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f),
                fontWeight = FontWeight.Light
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Loading...",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                EmotionPieChart(
                    data = emotionCounts,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
