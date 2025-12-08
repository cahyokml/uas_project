package com.example.uas_project.repository

import com.example.uas_project.api.RetrofitClient
import com.example.uas_project.data.Event

class EventRepository {
    private val api = RetrofitClient.api

    suspend fun getEvents(): List<Event> {
        val resp = api.getEvents()
        return resp.data ?: emptyList()
    }

    suspend fun getEvent(id: Int): Event? {
        val resp = api.getEventById(id)
        return resp.data
    }

    suspend fun createEvent(event: Event): Event? {
        val resp = api.createEvent(event)
        return resp.data
    }

    suspend fun updateEvent(id: Int, event: Event): Event? {
        val resp = api.updateEvent(id, event)
        return resp.data
    }

    suspend fun deleteEvent(id: Int): Boolean {
        val resp = api.deleteEvent(id)
        // server returns status and message; treat non-error status as success
        return resp.status in 200..299
    }
}
