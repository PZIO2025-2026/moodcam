package com.moodcam.frontend_android.ui.statistics

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.EmotionHistoryRepository
import com.moodcam.frontend_android.ui.components.charts.EmotionPieChart
import com.moodcam.frontend_android.ui.layouts.PremiumScreenLayout
import org.koin.compose.koinInject
import java.util.Date


@Composable
fun EmotionRecordStatistics(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
){
    val historyRepository = koinInject<EmotionHistoryRepository>()

    val uid = authViewModel.getUserId()
    var emotionCounts by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    var loading by remember { mutableStateOf(true) }
    var anchorDate by remember { mutableStateOf<Date?>(null) }

    fun refresh() {
        if (uid != null) {
            historyRepository.getRecent(uid,anchorDate, 7) { list ->
                emotionCounts = list.groupingBy { it.emotion }.eachCount()
                loading = false
            }
        } else {
            emotionCounts = emptyMap()
            loading = false
        }
    }
    LaunchedEffect(uid) { refresh() }

    PremiumScreenLayout(modifier=modifier){
        Text(
            text = "HISTORY",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))
        if (loading) {
            CircularProgressIndicator(color = Color(0xFF8B5CF6))
            return@PremiumScreenLayout
        }
        EmotionPieChart(emotionCounts)
    }
}