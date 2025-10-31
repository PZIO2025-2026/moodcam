package com.moodcam.frontend_android.db.entities

import com.google.firebase.Timestamp

data class EmotionRecord(
    val emotion: String,
    val createdAt: Timestamp
)
