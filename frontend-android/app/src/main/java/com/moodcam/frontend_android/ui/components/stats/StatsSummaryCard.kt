package com.moodcam.frontend_android.ui.components.stats

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Summary card showing key statistics (Total, Most common, Least common)
 */
@Composable
fun StatsSummaryCard(
    emotionCounts: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    val total = emotionCounts.values.sum()
    val mostCommon = emotionCounts.maxByOrNull { it.value }
    val leastCommon = emotionCounts.minByOrNull { it.value }
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        color = Color.White.copy(alpha = 0.08f),
        tonalElevation = 0.dp,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF8B5CF6).copy(alpha = 0.3f),
                            Color(0xFF6366F1).copy(alpha = 0.3f)
                        )
                    )
                )
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                label = "Total",
                value = total.toString(),
                modifier = Modifier.weight(1f)
            )
            
            mostCommon?.let {
                StatItem(
                    label = "Most",
                    value = it.key,
                    modifier = Modifier.weight(1f)
                )
            }
            
            leastCommon?.let {
                StatItem(
                    label = "Least",
                    value = it.key,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}
