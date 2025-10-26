package com.moodcam.frontend_android.db

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

data class EmotionRecord(
    val emotion: String,
    val createdAt: Timestamp
)

class EmotionHistoryRepository(private val db: FirebaseFirestore) {

    fun addEmotion(uid: String, emotion: String, onComplete: (() -> Unit)? = null, onError: ((Exception) -> Unit)? = null) {
        val data = hashMapOf(
            "emotion" to emotion,
            "createdAt" to Timestamp.now()
        )
        db.collection("users").document(uid)
            .collection("emotions")
            .add(data)
            .addOnSuccessListener { onComplete?.invoke() }
            .addOnFailureListener { e -> onError?.invoke(e) }
    }

    fun getRecent(uid: String, limit: Long = 50, onResult: (List<EmotionRecord>) -> Unit) {
        db.collection("users").document(uid)
            .collection("emotions")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .addOnSuccessListener { snap ->
                val list = snap.documents.mapNotNull { doc ->
                    val emotion = doc.getString("emotion")
                    val ts = doc.getTimestamp("createdAt")
                    if (emotion != null && ts != null) EmotionRecord(emotion, ts) else null
                }
                onResult(list)
            }
            .addOnFailureListener { _ -> onResult(emptyList()) }
    }
}