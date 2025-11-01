package com.moodcam.frontend_android.db.entities

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

data class User(
    val id: String = "",
    val email: String = "",
    val name: String? = null,
    val photoUrl: String? = null,
    val userStartAge: Int? = null,
    val createdAt: Timestamp = Timestamp.now()
) {

    fun getDaysWithUs(): String {
        val now = System.currentTimeMillis()
        val createdMillis = createdAt.toDate().time
        val diffMillis = now - createdMillis
        val days = (diffMillis / (24 * 60 * 60 * 1000)).toInt()
        return "$days days"
    }

    fun getCurrentAge(): Int? {
        if (userStartAge == null) return null
        
        val now = System.currentTimeMillis()
        val createdMillis = createdAt.toDate().time
        val diffMillis = now - createdMillis
        val yearsPassedFloat = diffMillis / (365.25 * 24 * 60 * 60 * 1000)
        val yearsPassedInt = yearsPassedFloat.toInt()
        
        return userStartAge + yearsPassedInt
    }

    fun isProfileComplete(): Boolean {
        return name != null && userStartAge != null
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "email" to email,
            "name" to name,
            "photoUrl" to photoUrl,
            "userStartAge" to userStartAge,
            "createdAt" to createdAt
        )
    }

    companion object {
        fun fromDocument(document: DocumentSnapshot): User? {
            return document.toObject(User::class.java)
        }
    }
}

