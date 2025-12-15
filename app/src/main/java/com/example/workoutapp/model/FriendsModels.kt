package com.example.workoutapp.model

// BAGIAN : SHARON
data class AddFriendRequest(
    val username: String
)

data class SimpleResponse(
    val data: String
)

data class FriendItem(
    val id: Int,
    val username: String,
    val duration_streak: Int,
    val time_stamp: String
)

data class FriendsListResponse(
    val data: List<FriendItem>
)


data class SuggestItem(
    val id: Int,
    val username: String
)


data class SuggestionsResponse(
    val data: List<SuggestItem>
)

