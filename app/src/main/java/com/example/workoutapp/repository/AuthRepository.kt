package com.example.workoutapp.repository

import com.example.workoutapp.model.*
import com.example.workoutapp.network.ApiClient


class AuthRepository {

    private val api = ApiClient.instance
    // go to ApiClient file
    // api service (@POST("login") suspend fun login...) just a string no real connection
    // so it tells retrofit to create real @POST/@GET,etc so that apiservice can call the backend
    // returns : ApiService (with working @POST/@GET/@PUT,etc)

    suspend fun register(username: String, password: String): String {
        // suspend lets Kotlin pause the function safely and resume later
        val res = api.register(RegisterRequest(username, password))
        return res.data.token
    }

    suspend fun login(username: String, password: String): String {
        val res = api.login(LoginRequest(username, password))
        // go to apiservice func login
        return res.data.token
        // extracts just the JWT string (token)
        // go back to authviewmodel
    }
}

