package com.example.workoutapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.model.WorkoutItem
import com.example.workoutapp.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class UserExercise(
    val name: String,
    val time: Int,
    val reps: Int,
    val calories: Int,
    val timeStamp: String
)

class WorkoutViewModel : ViewModel() {
    private val repository = WorkoutRepository()
    private val _userExercises = MutableStateFlow<List<UserExercise>>(emptyList())
    val userExercises: StateFlow<List<UserExercise>> get() = _userExercises
    private val _workoutList = MutableStateFlow<List<WorkoutItem>>(emptyList())
    val workoutList: StateFlow<List<WorkoutItem>> = _workoutList
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _errorMessage = MutableStateFlow<String?>(null)

    fun getWorkouts(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val data = repository.getWorkoutList(token)
                _workoutList.value = data
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addExercise(token: String, item: WorkoutItem, timeInput: String, repInput: String) {
        val timeVal = timeInput.toIntOrNull() ?: 0
        val repVal = repInput.toIntOrNull() ?: 0
        val caloriesVal = timeVal * item.calories_per_minute

        val currentTimestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date())

        val newExercise = UserExercise(
            name = item.workout_name,
            time = timeVal,
            reps = repVal,
            calories = caloriesVal,
            timeStamp = currentTimestamp
        )

        viewModelScope.launch {
            val currentList = _userExercises.value
            _userExercises.value = currentList + newExercise

            try {
                repository.addWorkout(token, item.id, timeVal, repVal)
            } catch (e: Exception) {
                _errorMessage.value = "Gagal upload: ${e.message}"
                println("Gagal upload: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadHistory(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val historyList = repository.getHistory(token)

                val mappedList = historyList.map { historyItem ->
                    UserExercise(
                        name = historyItem.workoutName,
                        time = historyItem.duration,
                        reps = historyItem.reps,
                        calories = historyItem.calories,
                        timeStamp = historyItem.timeStamp
                    )
                }

                _userExercises.value = mappedList

            } catch (e: Exception) {
                println("Gagal ambil history: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearData() {
        _userExercises.value = emptyList()
        _workoutList.value = emptyList()
    }


    fun formatDateForHeader(isoString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(isoString) ?: Date()

            val outputFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            isoString
        }
    }
}