package com.moodcam.frontend_android.ui.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.EmotionHistoryRepository
import com.moodcam.frontend_android.helpers.date.calculateWeekRange
import com.moodcam.frontend_android.ui.components.charts.EmotionPieChart
import com.moodcam.frontend_android.ui.components.stats.StatsSummaryCard
import com.moodcam.frontend_android.ui.components.stats.WeekNavigationCard
import com.moodcam.frontend_android.ui.layouts.PremiumScreenLayout
import org.koin.compose.koinInject
import java.util.*

@Composable
fun EmotionRecordStatistics(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    val historyRepository = koinInject<EmotionHistoryRepository>()
    val uid = authViewModel.getUserId()
    
    var emotionCounts by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    var loading by remember { mutableStateOf(true) }
    var weeksAgo by remember { mutableIntStateOf(0) }
    
    val (weekStart, weekEnd) = remember(weeksAgo) {
        calculateWeekRange(weeksAgo)
    }
    
    val scrollState = rememberScrollState()

    fun refresh() {
        if (uid != null) {
            loading = true
            historyRepository.getRecent(uid, weekEnd, 100) { list ->
                val filtered = list.filter { 
                    it.createdAt.toDate() >= weekStart && it.createdAt.toDate() <= weekEnd
                }
                emotionCounts = filtered.groupingBy { it.emotion }.eachCount()
                loading = false
            }
        } else {
            emotionCounts = emptyMap()
            loading = false
        }
    }
    
    LaunchedEffect(uid, weeksAgo) { refresh() }

    PremiumScreenLayout(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Text(
                text = "STATISTICS",
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
            
            // Week Navigation
            WeekNavigationCard(
                weeksAgo = weeksAgo,
                weekStart = weekStart,
                weekEnd = weekEnd,
                onPreviousWeek = { weeksAgo++ },
                onNextWeek = { if (weeksAgo > 0) weeksAgo-- },
                onBackToCurrentWeek = { weeksAgo = 0 }
            )
            
            // Statistics Summary
            if (!loading && emotionCounts.isNotEmpty()) {
                StatsSummaryCard(emotionCounts)
            }
            
            // Emotion Distribution Chart
            Column {
                Text(
                    text = "Emotion Distribution",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${emotionCounts.values.sum()} total detections",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF8B5CF6))
                }
            } else {
                EmotionPieChart(emotionCounts)
            }
            
            // Bottom padding
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
