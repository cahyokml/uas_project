package com.example.uas_project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.uas_project.data.Event
import com.example.uas_project.viewmodel.EventViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventFormScreen(eventId: Int? = null, onSaved: () -> Unit, viewModel: EventViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }

    // Observe events state once and prefill when editing
    val eventsState by viewModel.events.collectAsState()
    LaunchedEffect(eventId, eventsState) {
        eventId?.let { id ->
            val ev = eventsState.find { it.id == id }
            ev?.let { e ->
                title = e.title
                description = e.description ?: ""
                date = e.date ?: ""
                time = e.time ?: ""
                location = e.location ?: ""
                capacity = e.capacity?.toString() ?: ""
            }
        }
    }

    val loading by viewModel.loading.collectAsState()

    val scroll = rememberScrollState()
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(if (eventId != null) "Edit Event" else "Create Event") })
    }) { inner ->
        Column(modifier = Modifier.padding(inner).padding(16.dp).verticalScroll(scroll)) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Title, contentDescription = null) }
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Time (HH:MM:SS)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) }
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = capacity,
                onValueChange = { capacity = it },
                label = { Text("Capacity") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.People, contentDescription = null) }
            )
            Spacer(Modifier.height(12.dp))
            Button(onClick = {
                val capacityInt = capacity.toIntOrNull()
                val event = Event(title = title, description = description, date = date, time = time, location = location, capacity = capacityInt, status = "upcoming")
                if (eventId != null) {
                    viewModel.updateEvent(eventId, event) { updated -> if (updated != null) onSaved() }
                } else {
                    viewModel.createEvent(event) { created -> if (created != null) onSaved() }
                }
            }, enabled = !loading && title.isNotBlank(), modifier = Modifier.fillMaxWidth()) {
                Text(text = if (loading) "Saving..." else if (eventId != null) "Update" else "Save")
            }
        }
    }
}
