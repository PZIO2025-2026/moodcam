/** Floating action button used to persist current detected emotion.
 * Disabled while detecting or when no face.
 */

package com.moodcam.frontend_android.ui.components.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** @param onClick save callback.
 * @param isEnabled true when emotion can be saved.
 * @param modifier optional modifier.
 */
@Composable
fun SaveEmotionButton(
    onClick: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.size(72.dp),
        shape = CircleShape,
        color = Color.Black.copy(alpha = 0.6f),
        tonalElevation = 0.dp,
        shadowElevation = 16.dp,
        enabled = isEnabled
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = if (isEnabled) {
                            listOf(
                                Color(0xFF8B5CF6).copy(alpha = 0.8f),
                                Color(0xFF6366F1).copy(alpha = 0.6f)
                            )
                        } else {
                            listOf(
                                Color.Gray.copy(alpha = 0.4f),
                                Color.Gray.copy(alpha = 0.3f)
                            )
                        }
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Save,
                contentDescription = "Save emotion",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
