package com.example.workoutapp.model


data class WorkoutItem(
    val id : Int,
    val workout_name : String,
    val description : String?,
    val calories_per_minute : Int
)

data class WorkoutListResponse(
    val data : List<WorkoutItem>
)

data class AddWorkoutRequest(
    val workoutListId: Int,
    val duration : Int,
    val reps : Int
)

data class AddWorkoutResponse(
    val message : String,
    val data : WorkoutHistoryItem? = null
)

data class WorkoutHistoryItem(
    val id: Int,
    val workoutName: String,
    val duration: Int,
    val reps: Int,
    val calories: Int,

    val timeStamp: String
)

data class WorkoutHistoryResponse(
    val data: List<WorkoutHistoryItem>
)