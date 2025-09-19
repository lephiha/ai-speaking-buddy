package com.example.aispeakingbuddy.repository

import android.util.Patterns
import com.example.aispeakingbuddy.configuration.HTTPRequest
import com.example.aispeakingbuddy.configuration.HTTPService
import com.example.aispeakingbuddy.container.LoginRequest
import com.example.aispeakingbuddy.container.LoginResponse
import com.example.aispeakingbuddy.container.SignUpResponse
import com.example.aispeakingbuddy.container.UserResponse
import com.example.aispeakingbuddy.signuppage.RegistrationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository {
    private val api = HTTPService.getInstance().create(HTTPRequest::class.java)

    fun login(request: LoginRequest, callback: (LoginResponse) -> Unit) {
        api.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                callback(response.body() ?: LoginResponse("error", "Unknown error"))
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(LoginResponse("error", t.message ?: "Network error"))
            }
        })
    }

    fun register(request: UserResponse, callback: (SignUpResponse) -> Unit) {
        api.register(request).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                callback(response.body() ?: SignUpResponse("error", "Unknown error"))
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                callback(SignUpResponse("error", t.message ?: "Network error"))
            }
        })
    }

    suspend fun verifyToken(token: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // TODO: Implement actual token verification
                delay(1000)

                when {
                    token.contains("invalid") -> false
                    token.contains("expired") -> false
                    else -> true
                }

            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // TODO: Replace with actual API call
                // Example:
                // val request = ForgotPasswordRequest(email)
                // val response = apiService.sendPasswordReset(request)
                // return response.isSuccessful

                // Simulate network call
                delay(1500)

                // For demo: simulate different scenarios
                when {
                    email.contains("notfound") -> false
                    email.isEmpty() -> false
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> false
                    else -> true // Assume success for demo
                }

            } catch (e: Exception) {
                throw e
            }
        }
    }
}
