package com.example.uas_project.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_project.data.Event
import com.example.uas_project.viewmodel.EventViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh

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

    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Music Events") },
                actions = {
                    IconButton(onClick = { viewModel.loadEvents() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreate) {
                Icon(Icons.Default.Add, contentDescription = "Create")
            }
        }
    ) { inner ->
        Box(modifier = modifier.padding(inner)) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                return@Box
            }
            if (!error.isNullOrEmpty()) {
                Text(text = "Error: $error", modifier = Modifier.align(Alignment.Center))
                return@Box
            }

            LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                items(events) { e ->
                    EventRow(event = e, onClick = { e.id?.let(onOpenDetail) })
                }
            }
        }
    }
}



@Composable
fun EventRow(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            // Left: color box or placeholder
            Surface(
                modifier = Modifier.size(56.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = (event.title.firstOrNull() ?: '?').toString(), fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = event.title, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = event.date ?: "TBA", style = MaterialTheme.typography.bodySmall)
                    Text(text = event.time ?: "--:--", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Status chip
            Column(horizontalAlignment = Alignment.End) {
                val status = event.status ?: "unknown"
                AssistChip(onClick = {}, label = { Text(status.capitalize()) })
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Cap: ${event.capacity ?: "-"}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
