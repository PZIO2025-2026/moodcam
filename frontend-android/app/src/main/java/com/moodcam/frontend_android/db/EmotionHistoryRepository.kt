/** Firestore repository storing emotion records per user and retrieving recent history.
 */
package com.moodcam.frontend_android.db

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.moodcam.frontend_android.db.entities.EmotionRecord
import java.util.Date

/**
 * @class EmotionHistoryRepository
 * @brief Repository for managing emotion detection history in Firestore
 * 
 * Provides operations for storing and retrieving emotion detection records.
 * Emotion records are stored as a subcollection under each user's document.
 * 
 * @property db Firebase Firestore database instance
 */
class EmotionHistoryRepository(private val db: FirebaseFirestore) {

    /** Adds new emotion record with current timestamp.
     * @param uid user id.
     * @param emotion detected emotion label.
     * @param onComplete success callback.
     * @param onError error callback.
     */
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

    /** Retrieves recent emotion records ordered descending by timestamp.
     * @param uid user id.
     * @param anchorDate optional pagination anchor (records before date).
     * @param limit max records to fetch.
     * @param onResult callback with list of records (empty on failure).
     */
    fun getRecent(uid: String, anchorDate: Date?, limit: Long = 50, onResult: (List<EmotionRecord>) -> Unit) {
        db.collection("users").document(uid)
            .collection("emotions")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .whereLessThan("createdAt", anchorDate ?: Timestamp.now())
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