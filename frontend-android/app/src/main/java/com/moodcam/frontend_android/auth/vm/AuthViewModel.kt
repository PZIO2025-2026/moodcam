package com.moodcam.frontend_android.auth.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.core.logger.MESSAGE

class AuthViewModel : ViewModel(){
    private val auth : FirebaseAuth = FirebaseAuth.getInstance();
    private val _authState = MutableLiveData<AuthState>();
    val authState : LiveData<AuthState> = _authState;

    init {
        checkAuthStatus()
    }


    fun checkAuthStatus(){
            if(auth.currentUser == null){
                _authState.postValue(AuthState.Unauthenticated)
        }
            else{
                _authState.postValue(AuthState.Authenticated)
            }
    }

    fun login(email: String, password: String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.postValue(AuthState.Error("Email and password cannot be empty"))
            return
        }

        _authState.postValue(AuthState.Loading)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    _authState.postValue(AuthState.Authenticated)
                }
                else{
                    _authState.postValue(AuthState.Error(task.exception?.message ?: "Unknown error"))
                }
            }
    }

    fun signup(email: String, password: String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.postValue(AuthState.Error("Email and password cannot be empty"))
            return
        }

        _authState.postValue(AuthState.Loading)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    _authState.postValue(AuthState.Authenticated)
                }
                else{
                    _authState.postValue(AuthState.Error(task.exception?.message ?: "Unknown error"))
                }
            }
    }

    fun signout(){
        auth.signOut()
        _authState.postValue(AuthState.Unauthenticated)
    }

}

sealed class AuthState{

    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()

    data class Error(val message: String): AuthState()

}
