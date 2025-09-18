package com.example.aispeakingbuddy.repository

import android.util.Patterns
import com.example.aispeakingbuddy.configuration.HTTPRequest
import com.example.aispeakingbuddy.configuration.HTTPService
import com.example.aispeakingbuddy.container.LoginRequest
import com.example.aispeakingbuddy.container.LoginResponse
import com.example.aispeakingbuddy.container.SignUpResponse
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
        api.login(request.email, request.password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                callback(response.body() ?: LoginResponse("error", "Unknown error"))
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(LoginResponse("error", t.message ?: "Network error"))
            }
        })
    }

    suspend fun register(fullName: String, email: String, password: String): RegistrationResponse {
        return withContext(Dispatchers.IO) {
            try {
                // TODO: Replace with actual API call
                // Example:
                // val request = RegisterRequest(fullName, email, password)
                // val response = apiService.register(request)
                // return response.body() ?: throw Exception("Registration failed")

                // Simulate network call
                delay(2000)

                // For demo: simulate different scenarios
                when {
                    email.contains("existing") -> {
                        RegistrationResponse(
                            status = "error",
                            message = "Email này đã được sử dụng"
                        )
                    }
                    email.contains("invalid") -> {
                        RegistrationResponse(
                            status = "error",
                            message = "Email không hợp lệ"
                        )
                    }
                    password.length < 6 -> {
                        RegistrationResponse(
                            status = "error",
                            message = "Mật khẩu quá ngắn"
                        )
                    }
                    else -> {
                        RegistrationResponse(
                            status = "success",
                            message = "Đăng ký thành công! Vui lòng đăng nhập",
                            data = mapOf(
                                "user_id" to 12345,
                                "email" to email,
                                "full_name" to fullName
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                throw e
            }
        }
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
                // val response = apiService.sendPasswordResetEmail(email)
                // return response.isSuccessful

                // Simulate network call
                delay(1500)

                // For demo: simulate email sending
                // In real app, make HTTP request to your backend
                when {
                    email.contains("notfound") -> false
                    email.isEmpty() -> false
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> false
                    else -> true
                }

            } catch (e: Exception) {
                throw e
            }
        }
    }
}
