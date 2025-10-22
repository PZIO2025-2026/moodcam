package com.moodcam.frontend_android.ui.profile.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moodcam.frontend_android.ui.layouts.PremiumScreenLayout

@Composable
fun NameInputScreen(
    onCompleteClick: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    PremiumScreenLayout {
        Text(
            text = "Your Name",
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
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "What is your name?",
                    fontSize = 20.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name", color = Color.White.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White.copy(alpha = 0.9f),
                        focusedBorderColor = Color(0xFF8B5CF6),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        cursorColor = Color(0xFF8B5CF6),
                        focusedLabelColor = Color(0xFF8B5CF6)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onCompleteClick(name) },
                    enabled = name.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = if (name.isNotBlank()) {
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF8B5CF6),
                                        Color(0xFF6366F1)
                                    )
                                )
                            } else {
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Gray.copy(alpha = 0.3f),
                                        Color.Gray.copy(alpha = 0.3f)
                                    )
                                )
                            }
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp,
                        disabledElevation = 0.dp
                    )
                ) {
                    Text(
                        "Ready",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (name.isNotBlank()) Color.White else Color.White.copy(alpha = 0.5f)
                    )
                }

            }
        }
    }
}
