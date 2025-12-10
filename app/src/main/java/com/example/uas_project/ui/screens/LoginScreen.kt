package com.example.uas_project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.uas_project.viewmodel.AuthViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val loginState by authViewModel.loginState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(loginState) {
        if (loginState is com.example.uas_project.viewmodel.LoginState.Success) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        when (loginState) {
            is com.example.uas_project.viewmodel.LoginState.Loading -> CircularProgressIndicator()
            is com.example.uas_project.viewmodel.LoginState.Error -> Text((loginState as com.example.uas_project.viewmodel.LoginState.Error).message, color = MaterialTheme.colorScheme.error)
            else -> {}
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { authViewModel.login(username.trim(), password) }, modifier = Modifier.fillMaxWidth()) {
            Text("Sign in")
        }
    }
}
