package com.moodcam.frontend_android.ui.profile.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moodcam.frontend_android.ui.layouts.PremiumScreenLayout
import kotlin.math.roundToInt

@Composable
fun AgeSelectionScreen(
    onNextClick: (Int) -> Unit
) {
    var age by remember { mutableStateOf(25f) }

    PremiumScreenLayout {
        Text(
            text = "Your age",
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
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp)),
            color = Color.White.copy(alpha = 0.08f),
            tonalElevation = 0.dp,
            shadowElevation = 24.dp
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
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Text(
                    text = "${age.roundToInt()}",
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B5CF6)
                )

                Text(
                    text = "year old",
                    fontSize = 20.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Slider(
                        value = age,
                        onValueChange = { age = it },
                        valueRange = 12f..100f,
                        steps = 81,
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF8B5CF6),
                            activeTrackColor = Color(0xFF8B5CF6),
                            inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "12",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                        Text(
                            "100",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onNextClick(age.roundToInt()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF8B5CF6),
                                    Color(0xFF6366F1)
                                )
                            )
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp
                    )
                ) {
                    Text(
                        "Next",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
