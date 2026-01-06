package com.example.workoutapp.network

import com.example.workoutapp.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ==================== PUBLIC ROUTES ====================
    @POST("register")
    suspend fun register(@Body body: RegisterRequest): TokenResponse

    @POST("login")
    suspend fun login(@Body body: LoginRequest): TokenResponse

    // ==================== PRIVATE ROUTES - FRIENDS ====================
    @GET("private/friends/list")
    suspend fun getFriends(@Header("Authorization") token: String): FriendsListResponse

    @GET("private/friends/suggestions")
    suspend fun getSuggestions(@Header("Authorization") token: String): SuggestionsResponse

    @GET("private/friends/search")
    suspend fun searchUsers(
        @Header("Authorization") token: String,
        @Query("username") username: String
    ): SuggestionsResponse

    @POST("private/friends/add")
    suspend fun addFriend(
        @Header("Authorization") token: String,
        @Body body: AddFriendRequest
    ): SimpleResponse


    // ==================== PRIVATE ROUTES - STREAK (NEW!) ====================
    @POST("private/streak/checkin")
    suspend fun checkInStreak(
        @Header("Authorization") token: String,
        @Body body: StreakCheckInRequest
    ): Response<StreakCheckInApiResponse>

    @GET("private/streak/status/{friendId}")
    suspend fun getStreakStatus(
        @Header("Authorization") token: String,
        @Path("friendId") friendId: Int
    ): Response<StreakStatusApiResponse>


    @GET("private/workouts/list")
    suspend fun getWorkoutList(
        @Header("Authorization") token: String
    ): WorkoutListResponse

    @POST("private/workouts")
    suspend fun addWorkout(
        @Header("Authorization") token: String,
        @Body body: AddWorkoutRequest
    ): AddWorkoutResponse

    @GET("private/workouts/history")
    suspend fun getMyHistory(
        @Header("Authorization") token: String
    ): WorkoutHistoryResponse
}


//package com.example.workoutapp.network
//
//import com.example.workoutapp.model.*
//import retrofit2.http.*
//
//// BAGIAN : SHARON
//interface ApiService {
//
//    // public routes
//
//    @POST("register")
//    suspend fun register(@Body body: RegisterRequest): TokenResponse
//
//    @POST("login")
//    suspend fun login(@Body body: LoginRequest): TokenResponse
//    // suspend : makes the func runs w/o freezing the ui using coroutines
//    // LoginRequest : username, password
//    // TokenResponse : go to usermodel tokenresponse -> tokendata (make into string)
//    // go back to authrepository
//
//    // private routes (token required)
//    @GET("private/friends/list")
//    suspend fun getFriends(@Header("Authorization") token: String): FriendsListResponse // the json must match in the friendsmodel
//    // send token ("bearer $token")(in postman header in authorization)
//    @GET("private/friends/suggestions")
//    suspend fun getSuggestions(@Header("Authorization") token: String): SuggestionsResponse
//
//    @GET("private/friends/search")
//    suspend fun searchUsers(
//        @Header("Authorization") token: String,
//        @Query("username") username: String
//    ): SuggestionsResponse
//
//    @POST("private/friends/add")
//    suspend fun addFriend(
//        @Header("Authorization") token: String,
//        @Body body: AddFriendRequest
//    ): SimpleResponse
//}

