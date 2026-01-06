//koor dapur --> mau ambil makanan dr dapur mn (API?Cache?Database?)

package com.example.workoutapp.repository

import com.example.workoutapp.model.*
import com.example.workoutapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StreakRepository(private val apiService: ApiService) {

    /**
     * Check-in streak dengan teman
     * @param token JWT token (tanpa "Bearer ")
     * @param friendId ID teman yang mau di-check-in bareng
     * @return Result<StreakCheckInResponse>
     */
    suspend fun checkInStreak(token: String, friendId: Int): Result<StreakCheckInResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.checkInStreak(
                    token = "Bearer $token",
                    body = StreakCheckInRequest(friendId)
                )

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!.data)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Failed to check-in"
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Get streak status dengan teman tertentu
     * @param token JWT token
     * @param friendId ID teman
     * @return Result<StreakStatusResponse>
     */
    suspend fun getStreakStatus(token: String, friendId: Int): Result<StreakStatusResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getStreakStatus(
                    token = "Bearer $token",
                    friendId = friendId
                )

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!.data)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Failed to get streak status"
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}