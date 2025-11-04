package com.moodcam.frontend_android.ui.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Simple and beautiful Pie Chart for emotions using Compose Canvas
 */
@Composable
fun EmotionPieChart(
    data: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No data available",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
        return
    }

    val total = data.values.sum()
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(data) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pie Chart
        Box(
            modifier = Modifier
                .size(250.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasSize = size.minDimension
                val radius = canvasSize / 2
                val center = Offset(size.width / 2, size.height / 2)
                
                var startAngle = -90f
                
                data.entries.forEachIndexed { index, (emotion, count) ->
                    val sweepAngle = (count.toFloat() / total * 360f) * animationProgress.value
                    val color = EmotionColors.getColorForEmotion(emotion)
                    
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        topLeft = Offset(
                            center.x - radius,
                            center.y - radius
                        ),
                        size = Size(radius * 2, radius * 2)
                    )
                    
                    startAngle += sweepAngle
                }
                
                // White circle in center for donut effect (optional)
                drawCircle(
                    color = Color(0xFF0F0C29), // Match background
                    radius = radius * 0.5f,
                    center = center
                )
            }
            
            // Center text showing total
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = total.toString(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Total",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Legend
        EmotionLegend(data = data)
    }
}

