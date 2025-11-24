/** Temporary success message shown after saving an emotion. */

package com.moodcam.frontend_android.ui.components.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Temporary success message after saving an emotion. Auto dismisses after 2 seconds.
 *
 * @param onDismiss Callback invoked after auto-dismiss.
 * @param modifier Optional modifier for container.
 */
@Composable
fun SavedEmotionMessage(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        onDismiss()
    }
    
    Surface(
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        color = Color(0xFF10B981).copy(alpha = 0.9f),
        tonalElevation = 0.dp,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "✓ Emotion saved!",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}
