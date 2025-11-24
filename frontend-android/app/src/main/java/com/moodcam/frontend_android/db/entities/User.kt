/** Firestore-backed user profile entity with helper methods for derived metrics and conversions. */
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

    /** @return formatted number of days since account creation (e.g. "42 days"). */
    fun getDaysWithUs(): String {
        val now = System.currentTimeMillis()
        val createdMillis = createdAt.toDate().time
        val diffMillis = now - createdMillis
        val days = (diffMillis / (24 * 60 * 60 * 1000)).toInt()
        return "$days days"
    }

    /** Computes current age by adding elapsed full years to starting age (uses 365.25 days/year).
     * @return current age or null if starting age absent.
     */
    fun getCurrentAge(): Int? {
        if (userStartAge == null) return null
        
        val now = System.currentTimeMillis()
        val createdMillis = createdAt.toDate().time
        val diffMillis = now - createdMillis
        val yearsPassedFloat = diffMillis / (365.25 * 24 * 60 * 60 * 1000)
        val yearsPassedInt = yearsPassedFloat.toInt()
        
        return userStartAge + yearsPassedInt
    }

    /** @return true if both name and starting age are set. */
    fun isProfileComplete(): Boolean {
        return name != null && userStartAge != null
    }

    /** @return map representation for Firestore storage. */
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
        /** Creates user from Firestore document snapshot.
         * @param document snapshot.
         */
        fun fromDocument(document: DocumentSnapshot): User? {
            return document.toObject(User::class.java)
        }
    }
}

