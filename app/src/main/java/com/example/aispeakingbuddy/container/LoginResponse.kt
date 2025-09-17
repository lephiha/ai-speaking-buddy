package com.example.aispeakingbuddy.container

data class LoginResponse(
    val status: String,
    val message: String,
    val token: String? = null
)
