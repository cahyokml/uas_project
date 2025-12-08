package com.example.uas_project.api

import com.example.uas_project.data.Event
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Query

/**
 * The API at `api.php` returns a JSON envelope { status, message, data, timestamp }.
 * Here we expose two common methods:
 * - `getEventById` returns ApiResponse<Event> when called with `?id=...`
 * - `getEvents` returns ApiResponse<List<Event>> (if the API supports listing without id)
 *
 * Adjust query parameters as required by the server. If the server expects
 * different query names (e.g. action=list), update callers accordingly.
 */
interface ApiService {
    @GET("api.php")
    suspend fun getEventById(@Query("id") id: Int): ApiResponse<Event>

    @GET("api.php")
    suspend fun getEvents(): ApiResponse<List<Event>>

    @POST("api.php")
    suspend fun createEvent(@Body event: Event): ApiResponse<Event>

    @PUT("api.php")
    suspend fun updateEvent(@Query("id") id: Int, @Body event: Event): ApiResponse<Event>

    @DELETE("api.php")
    suspend fun deleteEvent(@Query("id") id: Int): ApiResponse<Any>
}
