package com.moodcam.frontend_android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    fun loadProfile() {
        val uid = authViewModel.getUserId()
        if (uid == null) {
            _profileState.value = ProfileState.Unauthenticated
            return
        }

        _profileState.value = ProfileState.Loading

        userRepository.checkIsProfileCompleted(uid) { isComplete ->
            if (isComplete) {
                userRepository.getProfile(uid) { name, age, days, email ->
                    _profileState.value = ProfileState.Loaded(
                        name = name ?: "User",
                        age = age ?: 25,
                        daysWithUs = days ?: "0 days",
                        email = email ?: "user@example.com",
                        isComplete = true
                    )
                }
            } else {
                _profileState.value = ProfileState.Loaded(
                    name = "User",
                    age = 25,
                    daysWithUs = "0 days",
                    email = "user@example.com",
                    isComplete = false
                )
            }
        }
    }

    fun saveProfile(name: String, age: Int) {
        val uid = authViewModel.getUserId() ?: return

        viewModelScope.launch {
            userRepository.saveProfile(uid, name, age)
            loadProfile() // Reload after save
        }
    }

    fun updateName(name: String) {
        val uid = authViewModel.getUserId() ?: return

        viewModelScope.launch {
            userRepository.updateName(uid, name)
            loadProfile() // Reload after update
        }
    }
}

sealed class ProfileState {
    object Loading : ProfileState()
    object Unauthenticated : ProfileState()
    data class Loaded(
        val name: String,
        val age: Int,
        val daysWithUs: String,
        val email: String,
        val isComplete: Boolean
    ) : ProfileState()
    data class Error(val message: String) : ProfileState()
}
