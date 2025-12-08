package com.example.uas_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas_project.ui.theme.Uas_projectTheme
import com.example.uas_project.ui.navigation.AppNavHost
import com.example.uas_project.viewmodel.EventViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Uas_projectTheme {
                val vm: EventViewModel = viewModel()
                AppNavHost(eventViewModel = vm)
            }
        }
    }
}

@Composable
fun AppMainPreview() {
    Uas_projectTheme {
        // preview placeholder
    }
}