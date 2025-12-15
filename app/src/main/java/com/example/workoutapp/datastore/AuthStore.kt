login&logout
package com.example.workoutapp.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.authDataStore by preferencesDataStore("auth_prefs")
// Context : this DataStore belongs to your MainActivity.
// authDataStore : This becomes a property you can access (AuthStore can access)
// Create or open a DataStore file named auth_prefs. (temporary save token, name if login / register haven't logout)

class AuthStore(private val context: Context) {

    companion object {
        val TOKEN = stringPreferencesKey("jwt_token")
        // stringPreferencesKey : A function provided by DataStore Preferences.  It creates a Preferences.Key<String>.
        // output : "jwt_token": "eyJhbGciOiJIUzI1NiIsInR..."
        val USERNAME = stringPreferencesKey("username")
    }

    suspend fun saveToken(token: String, username: String) {
        context.authDataStore.edit {
            // save token in auth_prefs because if in backend (database) (it can leaks, and if login with 3 devices, only save the latest device token, the previous 2 will break (token change))
            it[TOKEN] = token
            it[USERNAME] = username
        }
    }

    // LOGOUT
    suspend fun clearToken() {
        context.authDataStore.edit {
            it.remove(TOKEN)
            it.remove(USERNAME)
        }
    }

    // GET THE SAVED TOKEN & USERNAME OF THE USER THAT IS USING THE APP NOW
    // JUST OPENED (WILL CREATE DATASTORE) / LOG OUT : THE DATASTORE WILL BE EMPTY (CONTAINS TOKEN : "", USERNAME : USER)
    // AFTER LOGIN / REGISTER IN THE APP : THE DATASTORE WILL CONTAINS TOKEN & USERNAME (OF THE CURRENT USER)
    val getToken = context.authDataStore.data.map { prefs ->
        // context.authDataStore : get the data
        // .data : Flow <Preferences> (key->value)
        // .map  : Flow <String>
        // prefs -> : saying prefs diff from preferencesDataStore
        prefs[TOKEN] ?: "" // if token does not exist return ""
    }
    val getUsername = context.authDataStore.data.map {
        it[USERNAME] ?: "User"
    }
}

