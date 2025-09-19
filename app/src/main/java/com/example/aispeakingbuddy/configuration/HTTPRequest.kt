package com.example.aispeakingbuddy.configuration

import com.example.aispeakingbuddy.container.LoginRequest
import com.example.aispeakingbuddy.container.LoginResponse
import com.example.aispeakingbuddy.container.SignUpResponse
import com.example.aispeakingbuddy.container.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface HTTPRequest {

    @POST("api/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("api/register")
    fun register(@Body request: UserResponse): Call<SignUpResponse>
}
