package com.moodcam.frontend_android.ui.profile.edit

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.UserRepository
import com.moodcam.frontend_android.ui.layouts.PremiumScreenLayout

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,

    initialName: String,
    initialAge: String,
    initialEmail: String,
    isLoading: Boolean,
    isSaving: Boolean,
    externalError: String?,

    onSaveClicked: (newName: String) -> Unit,
    onCancelClicked: () -> Unit
) {
    var name by remember(initialName) { mutableStateOf(initialName) }

    var validationError by remember { mutableStateOf<String?>(null) }

    PremiumScreenLayout(modifier = modifier) {
        Text(
            text = "EDIT PROFILE",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            style = LocalTextStyle.current.copy(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFBFBFB),
                        Color(0xFFE0E0E0)
                    )
                )
            ),
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator(color = Color(0xFF8B5CF6))
            return@PremiumScreenLayout
        }

        val errorToShow = validationError ?: externalError
        if (errorToShow != null) {
            Text(text = errorToShow, color = Color.Red)
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
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
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it.take(50) },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF8B5CF6),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedLabelColor = Color(0xFF8B5CF6),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                OutlinedTextField(
                    value = initialAge,
                    onValueChange = {},
                    label = { Text("Age (read-only)") },
                    singleLine = true,
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Color.White.copy(alpha = 0.2f),
                        disabledLabelColor = Color.White.copy(alpha = 0.5f),
                        disabledTextColor = Color.White.copy(alpha = 0.7f)
                    )
                )

                OutlinedTextField(
                    value = initialEmail,
                    onValueChange = {},
                    label = { Text("Email (read-only)") },
                    singleLine = true,
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Color.White.copy(alpha = 0.2f),
                        disabledLabelColor = Color.White.copy(alpha = 0.5f),
                        disabledTextColor = Color.White.copy(alpha = 0.7f)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            val finalName = name.trim()
                            if (finalName.isEmpty()) {
                                validationError = "Please enter a valid name."
                            } else {
                                validationError = null
                                onSaveClicked(finalName)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isSaving,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
                    ) {
                        Text(if (isSaving) "Saving..." else "Save")
                    }

                    OutlinedButton(
                        onClick = { onCancelClicked() },
                        modifier = Modifier.weight(1f),
                        enabled = !isSaving,
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.dp
                        )
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}
