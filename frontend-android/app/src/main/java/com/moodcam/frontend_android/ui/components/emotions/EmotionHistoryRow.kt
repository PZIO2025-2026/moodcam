package com.moodcam.frontend_android.ui.components.emotions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moodcam.frontend_android.db.entities.EmotionRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun EmotionHistoryRow(rec: EmotionRecord) {
    val dateStr = remember(rec.createdAt) {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        df.format(Date(rec.createdAt.seconds * 1000))
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        color = Color.White.copy(alpha = 0.06f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    rec.emotion,
                    color = when (rec.emotion) {
                        "Happy" -> Color(0xFFFFD700)
                        "Sad" -> Color(0xFF6495ED)
                        "Angry" -> Color(0xFFFF4500)
                        "Surprise" -> Color(0xFFFF69B4)
                        "Fear" -> Color(0xFF9370DB)
                        "Disgust" -> Color(0xFF32CD32)
                        "Neutral" -> Color.White
                        else -> Color.White
                    },
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(dateStr, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
            // simple right-side accent
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .width(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color.White.copy(alpha = 0.2f))
            )
        }
    }
}