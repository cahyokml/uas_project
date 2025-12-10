package com.example.uas_project.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class AuthRepository(private val context: Context) {
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val USERNAME = stringPreferencesKey("username")

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[IS_LOGGED_IN] ?: false
    }

    val username: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USERNAME]
    }

    suspend fun login(username: String, password: String): Boolean {
        // Demo/local login: accept any non-empty username/password.
        val success = username.isNotBlank() && password.isNotBlank()
        if (success) {
            context.dataStore.edit { prefs ->
                prefs[IS_LOGGED_IN] = true
                prefs[USERNAME] = username
            }
        }
        return success
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = false
            prefs.remove(USERNAME)
        }
    }
}
