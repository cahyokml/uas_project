package com.example.uas_project.api

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    val status: Int,
    val message: String?,
    val data: T?,
    val timestamp: String?
)
