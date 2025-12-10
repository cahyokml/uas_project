package com.example.uas_project.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_project.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = AuthRepository(application.applicationContext)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _username = MutableStateFlow<String?>(null)
    val username: StateFlow<String?> = _username.asStateFlow()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    init {
        viewModelScope.launch {
            repo.isLoggedIn.collect { _isLoggedIn.value = it }
        }
        viewModelScope.launch {
            repo.username.collect { _username.value = it }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val ok = repo.login(username, password)
            if (ok) {
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error("Invalid credentials")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
            _loginState.value = LoginState.Idle
        }
    }
}
