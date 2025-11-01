package com.moodcam.frontend_android.db

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.moodcam.frontend_android.db.entities.User
import kotlinx.coroutines.tasks.await
import java.util.Date

class UserRepository(val db: FirebaseFirestore) {

    suspend fun createUserProfile(uid: String, email: String, authCreationTime: Long) {
        val user = User(
            id = uid,
            email = email,
            name = null,
            photoUrl = null,
            userStartAge = null,
            createdAt = Timestamp(Date(authCreationTime))
        )

        db.collection("users").document(uid)
            .set(user.toMap())
            .await()
    }

    fun saveProfile(uid: String, name: String, age: Int) {
        val updates = hashMapOf<String, Any>(
            "name" to name,
            "userStartAge" to age
        )

        db.collection("users").document(uid)
            .update(updates)
            .addOnSuccessListener {
                // TODO
            }
            .addOnFailureListener { e ->
                // TODO
            }
    }

    fun updateName(uid: String, name: String) {
        val updates = hashMapOf<String, Any>(
            "name" to name
        )

        db.collection("users").document(uid)
            .update(updates)
            .addOnSuccessListener {
                // TODO
            }
            .addOnFailureListener { e ->
                // TODO
            }
    }

    fun getProfile(uid: String, onResult: (User?) -> Unit) {
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                onResult(User.fromDocument(document))
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}