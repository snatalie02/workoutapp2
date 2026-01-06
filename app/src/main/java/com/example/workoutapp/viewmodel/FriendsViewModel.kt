package com.example.workoutapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.model.FriendItem
import com.example.workoutapp.model.SuggestItem
import com.example.workoutapp.repository.FriendsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// BAGIAN : SHARON (with minimal fix by MICHELLE)
class FriendsViewModel(
    private val repo: FriendsRepository
) : ViewModel() {

    private val _friends = MutableStateFlow(listOf<FriendItem>())
    val friends: StateFlow<List<FriendItem>> get() = _friends

    private val _suggest = MutableStateFlow(listOf<SuggestItem>())
    val suggest: StateFlow<List<SuggestItem>> get() = _suggest

    // ✅ NEW: Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun load(token: String) {
        viewModelScope.launch {
            try {
                _friends.value = repo.getFriends(token)
                _suggest.value = repo.getSuggestions(token)
            } catch (e: Exception) {
                // ✅ Catch error instead of crash
                _error.value = e.message ?: "Failed to load data"
            }
        }
    }

    fun search(token: String, query: String) {
        viewModelScope.launch {
            try {
                if (query.isNotEmpty()) {
                    _suggest.value = repo.search(token, query)
                } else {
                    _suggest.value = repo.getSuggestions(token)
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to search"
            }
        }
    }

    fun addFriend(token: String, username: String) {
        viewModelScope.launch {
            try {
                repo.addFriend(token, username)
                load(token) // Refresh
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to add friend"
            }
        }
    }

    // ✅ Clear error after showing
    fun clearError() {
        _error.value = null
    }
}