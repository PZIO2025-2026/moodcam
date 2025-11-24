/** Week selector card allowing navigation across weeks of data. */

package com.moodcam.frontend_android.ui.components.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

/** @param weeksAgo number of weeks offset (0=current).
 * @param weekStart start date of selected week.
 * @param weekEnd end date of selected week.
 * @param onPreviousWeek navigate to previous week.
 * @param onNextWeek navigate to next week (disabled if current).
 * @param onBackToCurrentWeek jump back to current week.
 * @param modifier optional modifier.
 */
@Composable
fun WeekNavigationCard(
    weeksAgo: Int,
    weekStart: Date,
    weekEnd: Date,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onBackToCurrentWeek: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd", Locale.getDefault()) }
    val isCurrentWeek = weeksAgo == 0
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        color = Color.White.copy(alpha = 0.08f),
        tonalElevation = 0.dp,
        shadowElevation = 12.dp
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
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Previous week button
                IconButton(
                    onClick = onPreviousWeek,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous week",
                        tint = Color.White
                    )
                }
                
                // Date range display
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = null,
                            tint = Color(0xFF8B5CF6),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = if (isCurrentWeek) "This Week" else "Week of ${dateFormat.format(weekStart)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${dateFormat.format(weekStart)} - ${dateFormat.format(weekEnd)}",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
                
                // Next week button (disabled for future weeks)
                IconButton(
                    onClick = onNextWeek,
                    enabled = weeksAgo > 0,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (weeksAgo > 0) Color.White.copy(alpha = 0.1f)
                            else Color.White.copy(alpha = 0.05f)
                        )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next week",
                        tint = if (weeksAgo > 0) Color.White else Color.White.copy(alpha = 0.3f)
                    )
                }
            }
            
            if (!isCurrentWeek) {
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = onBackToCurrentWeek,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Back to Current Week",
                        color = Color(0xFF8B5CF6),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
