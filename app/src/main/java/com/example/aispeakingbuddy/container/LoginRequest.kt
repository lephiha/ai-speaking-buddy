package com.example.aispeakingbuddy.container

data class LoginResponse(
    val status: String,
    val message: String?,
    val user: UserResponse?
)

data class UserResponse(
    val id: Int,
    val fullname: String,
    val email: String,
    val avatar: String?
)
