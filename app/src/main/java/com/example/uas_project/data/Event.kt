package com.example.uas_project.data

import com.google.gson.annotations.SerializedName

data class Event(
    val id: Int? = null,
    val title: String = "",
    val date: String? = null,
    val time: String? = null,
    val location: String? = null,
    val description: String? = null,
    val capacity: Int? = null,
    val status: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null
)
