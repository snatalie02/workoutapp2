package com.example.workoutapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutapp.datastore.AuthStore
import com.example.workoutapp.repository.AuthRepository
import com.example.workoutapp.repository.FriendsRepository
import com.example.workoutapp.repository.StreakRepository

/**
 * Factory untuk AuthViewModel
 * Inject AuthRepository dan AuthStore
 */
class AuthViewModelFactory(
    private val repository: AuthRepository,
    private val authStore: AuthStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository, authStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

/**
 * Factory untuk FriendsViewModel
 * Inject FriendsRepository
 */
class FriendsViewModelFactory(
    private val repository: FriendsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FriendsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

/**
 * Factory untuk StreakViewModel
 * Inject StreakRepository
 */
class StreakViewModelFactory(
    private val repository: StreakRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StreakViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StreakViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}