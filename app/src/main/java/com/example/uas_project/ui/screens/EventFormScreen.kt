package com.example.uas_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_project.data.Event
import com.example.uas_project.viewmodel.EventViewModel
import com.example.uas_project.ui.theme.*


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
        CenterAlignedTopAppBar(
            title = {
                Text(
                    if (eventId != null) "Edit Event" else "Create Event",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = VibrantOrange
            )
        )
    }) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(VibrantOrange.copy(alpha = 0.1f), VibrantPurple.copy(alpha = 0.1f))
                    )
                )
                .padding(16.dp)
                .verticalScroll(scroll)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Event Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                leadingIcon = { Icon(Icons.Default.Title, contentDescription = null, tint = VibrantOrange) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantOrange,
                    unfocusedBorderColor = VibrantOrange.copy(alpha = 0.5f)
                )
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .heightIn(min = 80.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantPurple,
                    unfocusedBorderColor = VibrantPurple.copy(alpha = 0.5f)
                )
            )

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date (YYYY-MM-DD)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantBlue,
                    unfocusedBorderColor = VibrantBlue.copy(alpha = 0.5f)
                )
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = VibrantGreen) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantGreen,
                    unfocusedBorderColor = VibrantGreen.copy(alpha = 0.5f)
                )
            )

            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Time (HH:MM:SS)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = VibrantCyan) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantCyan,
                    unfocusedBorderColor = VibrantCyan.copy(alpha = 0.5f)
                )
            )

            OutlinedTextField(
                value = capacity,
                onValueChange = { capacity = it },
                label = { Text("Capacity") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                leadingIcon = { Icon(Icons.Default.People, contentDescription = null, tint = VibrantPink) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantPink,
                    unfocusedBorderColor = VibrantPink.copy(alpha = 0.5f)
                )
            )

            Button(
                onClick = {
                    val capacityInt = capacity.toIntOrNull()
                    val event = Event(
                        title = title,
                        description = description,
                        date = date,
                        time = time,
                        location = location,
                        capacity = capacityInt,
                        status = "upcoming"
                    )
                    if (eventId != null) {
                        viewModel.updateEvent(eventId, event) { updated -> if (updated != null) onSaved() }
                    } else {
                        viewModel.createEvent(event) { created -> if (created != null) onSaved() }
                    }
                },
                enabled = !loading && title.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VibrantOrange,
                    disabledContainerColor = VibrantOrange.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = if (loading) "Saving..." else if (eventId != null) "Update Event" else "Create Event",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
