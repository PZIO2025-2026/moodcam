package com.moodcam.frontend_android.ui.components.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.moodcam.frontend_android.db.entities.EmotionRecord
import com.moodcam.frontend_android.ui.components.emotions.EmotionHistoryRow
import org.koin.compose.koinInject

/**
 * Card component showing recent emotion history list
 */
@Composable
fun RecentEmotionsCard(
    userId: String?,
    modifier: Modifier = Modifier,
    limit: Int = 10,
    historyRepository: EmotionHistoryRepository = koinInject()
) {
    var recentEmotions by remember { mutableStateOf<List<EmotionRecord>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        if (userId != null) {
            historyRepository.getRecent(userId, null, limit.toLong()) { emotions ->
                recentEmotions = emotions
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
                text = "Recent Activity",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = "${recentEmotions.size} recent detections",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f),
                fontWeight = FontWeight.Light
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Loading...",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else if (recentEmotions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No emotions detected yet",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Open camera to start tracking",
                            color = Color.Gray.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.heightIn(max = 400.dp)
                ) {
                    items(recentEmotions) { emotion ->
                        EmotionHistoryRow(emotion)
                    }
                }
            }
        }
    }
}
