package com.example.uas_project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_project.data.Event
import com.example.uas_project.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel(private val repo: EventRepository = EventRepository()) : ViewModel() {
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadEvents() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val list = repo.getEvents()
                _events.value = list
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _loading.value = false
            }
        }
    }

    fun createEvent(event: Event, onResult: (Event?) -> Unit = {}) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val created = repo.createEvent(event)
                // refresh
                loadEvents()
                onResult(created)
            } catch (e: Exception) {
                _error.value = e.message
                onResult(null)
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateEvent(id: Int, event: Event, onResult: (Event?) -> Unit = {}) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val updated = repo.updateEvent(id, event)
                loadEvents()
                onResult(updated)
            } catch (e: Exception) {
                _error.value = e.message
                onResult(null)
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteEvent(id: Int, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val ok = repo.deleteEvent(id)
                if (ok) loadEvents()
                onResult(ok)
            } catch (e: Exception) {
                _error.value = e.message
                onResult(false)
            } finally {
                _loading.value = false
            }
        }
    }
}
