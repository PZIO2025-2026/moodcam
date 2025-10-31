package com.moodcam.frontend_android.ui.statistics

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moodcam.frontend_android.db.EmotionHistoryRepository
import com.moodcam.frontend_android.ui.layouts.PremiumScreenLayout
import org.koin.compose.koinInject


@Composable
fun EmotionRecordStatistics(
    modifier: Modifier = Modifier
){
    val historyRepository = koinInject<EmotionHistoryRepository>()

    PremiumScreenLayout(modifier=modifier){
        // TODO : some kind of diagram here
    }
}