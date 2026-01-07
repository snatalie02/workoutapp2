package com.example.workoutapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.datastore.AuthStore
import com.example.workoutapp.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository,
    private val store: AuthStore
) : ViewModel() {

    private val _state = MutableStateFlow("")
    // private : cannot be accessed in ui so that cannot be changed, only in viewmodel
    // stateflow : data only read (cannot be edited / create)
    // if alrd lgin/reg : success/ error if haven't empty string ""
    val state: StateFlow<String> get() = _state
    // called by loginScreen (show the UI), calls _state

    fun register(username: String, password: String) {
        viewModelScope.launch {
            try {
                val token = repo.register(username, password)
                store.saveToken(token, username)
                _state.value = "success"
            } catch (e: Exception) {
                _state.value = "error: ${e.message}"
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val token = repo.login(username, password)
                // go to AuthRepository func login
                // output : username, pass kept in the database & bring back token
                store.saveToken(token, username)
                // go to AuthStore
                _state.value = "success" // when _state change to success loginscreen read state into success go back to login screen to go to home
            } catch (e: Exception) {
                _state.value = "error: ${e.message}"
            }
        }
    }


    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            store.clearToken()
            _state.value = ""
            onLoggedOut() // ke view login

        }
    }


}

