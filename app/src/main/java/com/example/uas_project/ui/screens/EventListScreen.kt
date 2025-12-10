package com.example.uas_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_project.data.Event
import com.example.uas_project.viewmodel.EventViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    viewModel: EventViewModel,
    onOpenDetail: (Int) -> Unit,
    onCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val events by viewModel.events.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadEvents() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Event Manager",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Magenta.copy(alpha = 0.9f)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreate,
                containerColor = Color(0xFF5A67F2) // elegant purple
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create", tint = Color.White)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF3F4F8)) // soft background
        ) {

            when {
                loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                !error.isNullOrEmpty() -> Text(
                    text = "Error: $error",
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(events) { e ->
                        EventCard(
                            event = e,
                            onClick = { e.id?.let(onOpenDetail) }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun EventCard(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Gradient avatar
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            listOf(Color(0xFF6C63FF), Color(0xFF8E97FD))
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    (event.title.firstOrNull() ?: '?').uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    event.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "${event.date ?: "TBA"}  •  ${event.time ?: "--:--"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "Capacity: ${event.capacity ?: "-"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6E6E6E)
                )
            }

            EventStatusBadge(event.status ?: "unknown")
        }
    }
}
@Composable
fun EventStatusBadge(status: String) {
    val (bg, color) = when (status.lowercase()) {
        "completed" -> Color(0xFFC9FFD9) to Color(0xFF0F8B3E)
        "cancelled" -> Color(0xFFFFD6D6) to Color(0xFFB90E0E)
        else        -> Color(0xFFCFD6FF) to Color(0xFF2836D4)
    }

    Box(
        modifier = Modifier
            .shadow(6.dp, RoundedCornerShape(14.dp))
            .background(bg, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = status.replaceFirstChar { it.uppercase() },
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}
