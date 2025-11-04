package com.moodcam.frontend_android.ui.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.compose.ui.unit.dp
import com.moodcam.frontend_android.ui.layouts.PremiumScreenLayout

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("settings", Context.MODE_PRIVATE) }

    var soundEnabled by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(false) }
    var highSensitivity by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        soundEnabled = prefs.getBoolean("soundEnabled", false)
        notificationsEnabled = prefs.getBoolean("notificationsEnabled", true)
        highSensitivity = prefs.getBoolean("highSensitivity", false)
    }

    PremiumScreenLayout(modifier = modifier) {
        Text("SETTINGS", style = MaterialTheme.typography.headlineMedium, color = Color.White)

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Enable sound", color = Color.White, modifier = Modifier.weight(1f))
                Switch(checked = soundEnabled, onCheckedChange = { soundEnabled = it })
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Notifications", color = Color.White, modifier = Modifier.weight(1f))
                Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("High sensitivity", color = Color.White, modifier = Modifier.weight(1f))
                Switch(checked = highSensitivity, onCheckedChange = { highSensitivity = it })
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    prefs.edit()
                        .putBoolean("soundEnabled", soundEnabled)
                        .putBoolean("notificationsEnabled", notificationsEnabled)
                        .putBoolean("highSensitivity", highSensitivity)
                        .apply()

                    Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT).show()
                    onNavigateUp()
                }, modifier = Modifier.weight(1f)) {
                    Text("Save")
                }

                OutlinedButton(onClick = { onNavigateUp() }, modifier = Modifier.weight(1f)) {
                    Text("Close")
                }
            }
        }
    }
}
