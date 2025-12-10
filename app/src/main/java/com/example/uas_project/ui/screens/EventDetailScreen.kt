package com.example.uas_project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_project.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(id: Int, viewModel: EventViewModel, onBack: () -> Unit, onEdit: (Int) -> Unit) {
    val event = viewModel.events.collectAsState().value.find { it.id == id }

    val scroll = rememberScrollState()
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = event?.title ?: "Event") },
            navigationIcon = {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            }
        )
    }) { inner ->
        Column(modifier = Modifier.padding(inner).padding(16.dp).verticalScroll(scroll)) {
            Text(text = event?.title ?: "-", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Default.Schedule, contentDescription = "Time")
                Column {
                    Text(text = event?.date ?: "TBA", style = MaterialTheme.typography.bodyMedium)
                    Text(text = event?.time ?: "--:--", style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(Icons.Default.Place, contentDescription = "Location")
                Text(text = event?.location ?: "TBA", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))

            Text(text = event?.description ?: "No description", style = MaterialTheme.typography.bodyLarge, maxLines = 10, overflow = TextOverflow.Ellipsis)

            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AssistChip(onClick = {}, label = { Text(text = event?.status?.capitalize() ?: "Unknown") })
                        Text(text = "Capacity: ${event?.capacity ?: "-"}")
                        Spacer(modifier = Modifier.width(8.dp))
                        // Actions: Edit / Delete
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = { event?.id?.let { onEdit(it) } }) { Text("Edit") }
                            TextButton(onClick = {
                                event?.id?.let { id ->
                                    viewModel.deleteEvent(id) { ok -> if (ok) onBack() }
                                }
                            }) { Text("Delete") }
                        }
            }
        }
    }
}
