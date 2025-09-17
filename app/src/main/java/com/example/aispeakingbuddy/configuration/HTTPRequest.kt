package com.example.aispeakingbuddy.configuration

import com.example.aispeakingbuddy.container.LoginResponse
import com.example.aispeakingbuddy.container.SignUpResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface HTTPRequest {

    @FormUrlEncoded
    @POST("api/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("api/register")
    fun register(
        @Field("fullname") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<SignUpResponse>
}
