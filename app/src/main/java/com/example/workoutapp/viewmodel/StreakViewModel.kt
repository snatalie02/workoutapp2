//pelayan

package com.example.workoutapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.model.StreakCheckInResponse
import com.example.workoutapp.model.StreakStatusResponse
import com.example.workoutapp.repository.StreakRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel untuk Streak Feature
 * Menggunakan StateFlow untuk lifecycle-aware state management
 */
class StreakViewModel(private val repository: StreakRepository) : ViewModel() {

    // ============== STATE MANAGEMENT ==============

    // State untuk streak status dengan teman tertentu
    private val _streakStatus = MutableStateFlow<UiState<StreakStatusResponse>>(UiState.Idle)
    val streakStatus: StateFlow<UiState<StreakStatusResponse>> = _streakStatus.asStateFlow()

    // State untuk check-in response
    private val _checkInResult = MutableStateFlow<UiState<StreakCheckInResponse>>(UiState.Idle)
    val checkInResult: StateFlow<UiState<StreakCheckInResponse>> = _checkInResult.asStateFlow()

    // ============== FUNCTIONS ==============

    /**
     * Check-in streak dengan teman
     * @param token JWT token dari AuthStore
     * @param friendId ID teman
     */
    fun checkInStreak(token: String, friendId: Int) {
        viewModelScope.launch {
            _checkInResult.value = UiState.Loading

            val result = repository.checkInStreak(token, friendId)

            _checkInResult.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull()!!)
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    /**
     * Get streak status dengan teman tertentu
     * @param token JWT token
     * @param friendId ID teman
     */
    fun getStreakStatus(token: String, friendId: Int) {
        viewModelScope.launch {
            _streakStatus.value = UiState.Loading

            val result = repository.getStreakStatus(token, friendId)

            _streakStatus.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull()!!)
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    /**
     * Reset check-in result state
     * Berguna untuk clear error message setelah ditampilkan
     */
    fun resetCheckInResult() {
        _checkInResult.value = UiState.Idle
    }
}

/**
 * Sealed class untuk UI State
 * Memudahkan handling berbagai state (Loading, Success, Error)
 */
sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}