package com.moodcam.frontend_android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.db.UserRepository
import com.moodcam.frontend_android.db.entities.User
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

        userRepository.getProfile(uid) { user ->
            if (user != null) {
                _profileState.value = ProfileState.Loaded(
                    user = user,
                    name = user.name ?: "User",
                    age = user.getCurrentAge() ?: user.userStartAge ?: 25,
                    daysWithUs = user.getDaysWithUs(),
                    email = user.email,
                    isComplete = user.isProfileComplete()
                )
            } else {
                _profileState.value = ProfileState.Error("Failed to load profile")
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
        val user: User,
        val name: String,
        val age: Int,
        val daysWithUs: String,
        val email: String,
        val isComplete: Boolean
    ) : ProfileState()
    data class Error(val message: String) : ProfileState()
}
