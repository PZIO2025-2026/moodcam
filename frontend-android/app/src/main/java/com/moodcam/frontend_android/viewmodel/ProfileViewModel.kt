/**
 * ViewModel providing user profile state (`ProfileState`) and operations to load,
 * save initial profile data and update the display name. Depends on `AuthViewModel`
 * for the current user id and delegates persistence to `UserRepository`.
 */
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

    /** Loads profile for current authenticated user updating `profileState`. */
    fun loadProfile() {
        val uid = authViewModel.getUserId()
        if (uid == null) {
            _profileState.value = ProfileState.Unauthenticated
            return
        }

        _profileState.value = ProfileState.Loading

        userRepository.getProfile(uid) { user ->
            if (user != null) {
                _profileState.value = ProfileState.Loaded(user)
            } else {
                _profileState.value = ProfileState.Error("Failed to load profile")
            }
        }
    }

    /** Saves initial profile (name, age) then reloads state.
     * @param name display name.
     * @param age starting age.
     */
    fun saveProfile(name: String, age: Int) {
        val uid = authViewModel.getUserId() ?: return

        viewModelScope.launch {
            userRepository.saveProfile(uid, name, age)
            loadProfile() // Reload after save
        }
    }

    /** Updates display name then reloads profile.
     * @param name new display name.
     */
    fun updateName(name: String) {
        val uid = authViewModel.getUserId() ?: return

        viewModelScope.launch {
            userRepository.updateName(uid, name)
            loadProfile() // Reload after update
        }
    }
}

/** Sealed profile states representing loading, unauthenticated, loaded and error. */
sealed class ProfileState {
    /** Loading in progress. */
    object Loading : ProfileState()
    
    /** User not authenticated. */
    object Unauthenticated : ProfileState()
    
    /** Successfully loaded profile data. */
    data class Loaded(val user: User) : ProfileState()
    
    /** Profile loading failed. */
    data class Error(val message: String) : ProfileState()
}
