package com.moodcam.frontend_android.ui.components.charts

import androidx.compose.ui.graphics.Color

/** Chart styling configuration and emotion color mappings. */

/** Configuration for basic chart appearance. */
data class ChartConfig(
    val barColor: Color = Color(0xFF8B5CF6),
    val barWidth: Float = 16f,
    val labelColor: Color = Color.White,
    val labelSize: Float = 12f,
    val showGrid: Boolean = false,
    val animationEnabled: Boolean = true
)

/** Centralized emotion color palette. */
object EmotionColors {
    val Happy = Color(0xFFFDD835)      // Yellow
    val Sad = Color(0xFF42A5F5)        // Blue
    val Angry = Color(0xFFEF5350)      // Red
    val Surprised = Color(0xFFFF9800)  // Orange
    val Neutral = Color(0xFF78909C)    // Gray
    val Fear = Color(0xFF9C27B0)       // Purple
    val Disgust = Color(0xFF66BB6A)    // Green
    
    /** Returns a color for the emotion (case-insensitive). */
    fun getColorForEmotion(emotion: String): Color {
        return when (emotion.lowercase()) {
            "happy" -> Happy
            "sad" -> Sad
            "angry" -> Angry
            "surprised" -> Surprised
            "neutral" -> Neutral
            "fear" -> Fear
            "disgust" -> Disgust
            else -> Color(0xFF8B5CF6) // Default purple
        }
    }
}
