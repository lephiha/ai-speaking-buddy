package com.example.aispeakingbuddy.configuration

import com.example.aispeakingbuddy.container.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface HTTPRequest {

    // LOGIN API
    @FormUrlEncoded
    @POST("api/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

//    // REGISTER API
//    @FormUrlEncoded
//    @POST("api/register")
//    fun register(
//        @Field("fullname") fullname: String,
//        @Field("email") email: String,
//        @Field("phone") phone: String,
//        @Field("password") password: String
//    ): Call<RegisterResponse>
}