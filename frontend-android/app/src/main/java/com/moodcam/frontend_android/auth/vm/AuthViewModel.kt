/**
 * ViewModel handling Firebase authentication: login, signup and signout.
 * Exposes authentication status via `AuthState` LiveData and caches the user id.
 */
package com.moodcam.frontend_android.auth.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.moodcam.frontend_android.db.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.core.logger.MESSAGE

/**
 * Handles Firebase Authentication operations: email/password login, registration,
 * logout and status checks.
 *
 * @property userRepository Repository used to create accompanying profile records.
 * @property auth Firebase Authentication instance.
 */
class AuthViewModel(private val userRepository: UserRepository, private val auth: FirebaseAuth) : ViewModel(){
    private val _authState = MutableLiveData<AuthState>();
    val authState : LiveData<AuthState> = _authState;
    private var userId : String? = null;

    init {
        checkAuthStatus()
    }

    /**
     * Updates `authState` and cached `userId` according to current Firebase user.
     */
    fun checkAuthStatus(){
            if(auth.currentUser == null){
                _authState.postValue(AuthState.Unauthenticated)
                userId = null
        }
            else{
                _authState.postValue(AuthState.Authenticated)
                userId = auth.currentUser?.uid
            }
    }

    /**
     * Performs email/password login updating `authState` to Loading, Authenticated or Error.
     * @param email user email.
     * @param password user password.
     */
    fun login(email: String, password: String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.postValue(AuthState.Error("Email and password cannot be empty"))
            return
        }

        _authState.postValue(AuthState.Loading)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    userId = firebaseUser?.uid.toString()
                    _authState.postValue(AuthState.Authenticated)
                } else {
                    _authState.postValue(
                        AuthState.Error(task.exception?.message ?: "Unknown error")
                    )
                }
            }
    }

    /**
     * Registers a new user then creates a Firestore profile document.
     * @param email user email.
     * @param password user password.
     */
    fun signup(email: String, password: String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.postValue(AuthState.Error("Email and password cannot be empty"))
            return
        }

        _authState.postValue(AuthState.Loading)

        viewModelScope.launch {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()

                val firebaseUser = authResult.user
                if (firebaseUser == null) {
                    throw IllegalStateException("User is null after successful registration")
                }
                val creationTime = firebaseUser.metadata?.creationTimestamp
                if (creationTime == null) {
                    throw IllegalStateException("User metadata is null")
                }
                userId = firebaseUser.uid
                userRepository.createUserProfile(
                    uid = firebaseUser.uid,
                    email = email,
                    authCreationTime = creationTime,
                )
                _authState.postValue(AuthState.Authenticated)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Signup Failed", e)
                _authState.postValue(AuthState.Error(e.message ?: "Unknown error"))
            }
        }
    }

    /** Signs out current Firebase user and sets state to Unauthenticated. */
    fun signout(){
        auth.signOut()
        _authState.postValue(AuthState.Unauthenticated)
        userId = null;
    }

    /** @return current user id or null if unauthenticated. */
    fun getUserId(): String? {
        return userId;
    }

}

/** Sealed authentication states exposed by `AuthViewModel`. */
sealed class AuthState{
    /** User is successfully authenticated. */
    object Authenticated : AuthState()
    
    /** No user is currently authenticated. */
    object Unauthenticated : AuthState()
    
    /** Authentication operation in progress. */
    object Loading : AuthState()
    
    /** Authentication operation failed with error message. */
    data class Error(val message: String): AuthState()

}
