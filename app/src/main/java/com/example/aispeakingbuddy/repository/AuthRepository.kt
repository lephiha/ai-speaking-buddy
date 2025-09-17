package com.example.aispeakingbuddy.repository

import com.example.aispeakingbuddy.configuration.HTTPRequest
import com.example.aispeakingbuddy.configuration.HTTPService
import com.example.aispeakingbuddy.container.LoginRequest
import com.example.aispeakingbuddy.container.LoginResponse
import com.example.aispeakingbuddy.container.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository {
    private val api = HTTPService.getInstance().create(HTTPRequest::class.java)

    fun login(request: LoginRequest, callback: (LoginResponse) -> Unit) {
        api.login(request.email, request.password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                callback(response.body() ?: LoginResponse("error", "Unknown error"))
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(LoginResponse("error", t.message ?: "Network error"))
            }
        })
    }

    fun register(fullname: String, email: String, password: String, callback: (SignUpResponse) -> Unit) {
        api.register(fullname, email, password).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                callback(response.body() ?: SignUpResponse("error", "Unknown error"))
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                callback(SignUpResponse("error", t.message ?: "Network error"))
            }
        })
    }
}
