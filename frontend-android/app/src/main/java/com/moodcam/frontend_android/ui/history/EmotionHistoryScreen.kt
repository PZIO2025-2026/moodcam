package com.moodcam.frontend_android.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.EmotionHistoryRepository
import com.moodcam.frontend_android.db.EmotionRecord
import com.moodcam.frontend_android.ui.layouts.PremiumScreenLayout
import org.koin.androidx.compose.get
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EmotionHistoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    historyRepository: EmotionHistoryRepository = get()
) {
    val uid = authViewModel.getUserId()
    var records by remember { mutableStateOf<List<EmotionRecord>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    fun refresh() {
        if (uid != null) {
            historyRepository.getRecent(uid, 50) { list ->
                records = list
                loading = false
            }
        } else {
            records = emptyList()
            loading = false
        }
    }

    LaunchedEffect(uid) { refresh() }

    PremiumScreenLayout(modifier = modifier) {
        Text(
            text = "HISTORY",
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
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (loading) {
            CircularProgressIndicator(color = Color(0xFF8B5CF6))
            return@PremiumScreenLayout
        }

        if (records.isEmpty()) {
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
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.History,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        "No history yet",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "Use the camera to analyze and track your emotions.",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    OutlinedButton(onClick = { refresh() }) {
                        Text("Refresh")
                    }
                }
            }
            return@PremiumScreenLayout
        }

        // List of records
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
                    .padding(8.dp)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(records) { rec ->
                        EmotionHistoryRow(rec)
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        OutlinedButton(onClick = { refresh() }) { Text("Refresh") }
    }
}

@Composable
private fun EmotionHistoryRow(rec: EmotionRecord) {
    val dateStr = remember(rec.createdAt) {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        df.format(Date(rec.createdAt.seconds * 1000))
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        color = Color.White.copy(alpha = 0.06f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    rec.emotion,
                    color = when (rec.emotion) {
                        "Happy" -> Color(0xFFFFD700)
                        "Sad" -> Color(0xFF6495ED)
                        "Angry" -> Color(0xFFFF4500)
                        "Surprise" -> Color(0xFFFF69B4)
                        "Fear" -> Color(0xFF9370DB)
                        "Disgust" -> Color(0xFF32CD32)
                        "Neutral" -> Color.White
                        else -> Color.White
                    },
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(dateStr, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
            // simple right-side accent
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .width(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color.White.copy(alpha = 0.2f))
            )
        }
    }
}