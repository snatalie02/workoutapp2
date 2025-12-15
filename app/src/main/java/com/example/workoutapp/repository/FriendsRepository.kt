package com.example.workoutapp.repository

import com.example.workoutapp.model.*
import com.example.workoutapp.network.ApiClient

// BAGIAN : SHARON
class FriendsRepository {

    private val api = ApiClient.instance

    suspend fun getFriends(token: String) =
        api.getFriends("Bearer $token").data // return the friends list data to friendsvm

    suspend fun getSuggestions(token: String) =
        api.getSuggestions("Bearer $token").data // return the suggestions list data to friendsvm

    suspend fun search(token: String, query: String) =
        api.searchUsers("Bearer $token", query).data

    suspend fun addFriend(token: String, username: String) =
        api.addFriend("Bearer $token", AddFriendRequest(username))
}

