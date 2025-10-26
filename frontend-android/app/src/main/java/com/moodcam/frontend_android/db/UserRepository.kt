package com.moodcam.frontend_android.db

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

class UserRepository(val db: FirebaseFirestore) {

    suspend fun createUserProfile(uid: String, email: String, authCreationTime: Long) {

        val creationTimestamp = Timestamp(Date(authCreationTime))

        val userProfile = hashMapOf(
            "id" to uid,
            "email" to email,
            "createdAt" to creationTimestamp,
            "name" to null,
            "photoUrl" to null,
            "userStartAge" to null
        )

        db.collection("users").document(uid)
            .set(userProfile)
            .await()
    }

    fun checkIsProfileCompleted(uid: String, onResult: (Boolean) -> Unit) {
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                val isComplete = document.get("name") != null && document.get("userStartAge") != null
                onResult(isComplete)
            }
            .addOnFailureListener {
                onResult(false)
            }
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

    fun getProfile(uid: String, onResult: (name: String?, age: Int?, dateWithUs: String?, userEmail: String? ) -> Unit) {
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                val name = document.getString("name")
                val createdAt = document.getTimestamp("createdAt")
                val userStartAge = document.getLong("userStartAge")?.toInt()

                val dateWithUs = calculateDaysWithUs(createdAt)

                val email = document.getString("email")

                val currentAge = if (createdAt != null && userStartAge != null) {
                    calculateCurrentAge(createdAt, userStartAge)
                } else {
                    userStartAge
                }

                onResult(name, currentAge, dateWithUs, email)
            }
            .addOnFailureListener {
                onResult(null, null, null, null)
            }
    }

    private fun calculateDaysWithUs(createdAt: Timestamp?): String {

        if(createdAt == null) {
            return "30 days"
        }

        val now = System.currentTimeMillis()
        val createdMillis: Long = createdAt.toDate().time

        val diffMillis = now - createdMillis

        val yearsPassedInt = (diffMillis / (24 * 60 * 60 * 1000)).toInt();

        val returnString = yearsPassedInt.toString() + " days"

        return returnString
    }

    private fun calculateCurrentAge(createdAt: Timestamp, startAge: Int): Int {
        val now = System.currentTimeMillis()
        val createdMillis = createdAt.toDate().time

        val diffMillis = now - createdMillis

        val yearsPassedFloat = diffMillis / (365.25 * 24 * 60 * 60 * 1000)
        val yearsPassed = yearsPassedFloat.toInt()

        return startAge + yearsPassed
    }


}