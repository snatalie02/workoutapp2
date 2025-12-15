package com.example.workoutapp.model

// BAGIAN : SHARON
data class RegisterRequest(
    val username: String,
    val password: String
)

// BAGIAN : SHARON
data class LoginRequest(
    val username: String,
    val password: String
)

data class TokenResponse(
    val data: TokenData
)

// make it into string
data class TokenData(
    val token: String
)