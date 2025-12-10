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
import androidx.core.view.WindowCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge UI (status bar & navigation bar transparan)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge()

        setContent {
            Uas_projectTheme {
                val eventVM: EventViewModel = viewModel()
                AppMainScreen(eventVM)
            }
        }
    }
}

@Composable
fun AppMainScreen(eventViewModel: EventViewModel) {
    AppNavHost(eventViewModel = eventViewModel)
}
