/** Firestore repository for creating, updating and retrieving user profile documents.
 * Used by onboarding and profile management flows.
 */
package com.moodcam.frontend_android.db

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.moodcam.frontend_android.db.entities.User
import kotlinx.coroutines.tasks.await
import java.util.Date

/**
 * @class UserRepository
 * @brief Repository for managing user profile data in Firestore
 * 
 * Provides CRUD operations for user profiles stored in Firebase Firestore.
 * Handles user profile creation, updates, and retrieval.
 * 
 * @property db Firebase Firestore database instance
 */
class UserRepository(val db: FirebaseFirestore) {

    /** Creates initial user profile document after signup.
     * @param uid firebase auth id.
     * @param email user email.
     * @param authCreationTime account creation epoch millis.
     */
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

    /** Updates profile with name and starting age (onboarding).
     * @param uid user id.
     * @param name display name.
     * @param age starting age.
     */
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

    /** Updates only the display name.
     * @param uid user id.
     * @param name new display name.
     */
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

    /** Retrieves user profile asynchronously.
     * @param uid user id.
     * @param onResult callback with User or null.
     */
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