package com.example.workoutapp.repository

import com.example.workoutapp.model.AddWorkoutRequest
import com.example.workoutapp.network.ApiClient

class WorkoutRepository {

    private val api = ApiClient.instance
    suspend fun getWorkoutList(token: String) =
        api.getWorkoutList("Bearer $token").data
    suspend fun addWorkout(token: String, workoutId: Int, duration: Int, reps: Int) =
        api.addWorkout(
            token = "Bearer $token",
            body = AddWorkoutRequest(workoutId, duration, reps)
        )

    suspend fun getHistory(token: String) =
        api.getMyHistory("Bearer $token").data
}