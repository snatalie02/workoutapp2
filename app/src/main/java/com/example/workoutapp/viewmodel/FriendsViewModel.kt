package com.example.workoutapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.repository.FriendsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// BAGIAN : SHARON
class FriendsViewModel(
    private val repo: FriendsRepository
) : ViewModel() {

    private val _friends = MutableStateFlow(listOf<com.example.workoutapp.model.FriendItem>())
    // diff with login : <string> and the func bellow only give "success" or "error..."
    // <FriendsModel> (id : int, username : string, etc)
    // bellow give the list data from api
    val friends: StateFlow<List<com.example.workoutapp.model.FriendItem>> get() = _friends
    // flow krn data berubah & dibuat jadi state di ui add friends

    private val _suggest = MutableStateFlow(listOf<com.example.workoutapp.model.SuggestItem>())
    val suggest: StateFlow<List<com.example.workoutapp.model.SuggestItem>> get() = _suggest

    fun load(token: String) {
        viewModelScope.launch {
            _friends.value = repo.getFriends(token)
            _suggest.value = repo.getSuggestions(token)
        }
    }

    fun search(token: String, query: String) {
        // query is it (what the user type )
        viewModelScope.launch {
            if (query.isNotEmpty()) { // if the user searched something (type something)
                _suggest.value = repo.search(token, query)
                //
            } else { // if the searched is empty (don't search anything) just make the suggestion list like the load (show all that is not friends & user)
                _suggest.value = repo.getSuggestions(token)
            }
        }
    }

    fun addFriend(token: String, username: String) {
        // username of your friend
        viewModelScope.launch {
            repo.addFriend(token, username) // fix the new friends & suggestion data
            load(token) // call the load again to refresh the val suggest & friends to match the new list
        }
    }
}


