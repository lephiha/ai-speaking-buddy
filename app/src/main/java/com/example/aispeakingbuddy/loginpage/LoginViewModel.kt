package com.example.aispeakingbuddy.loginpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aispeakingbuddy.container.LoginRequest
import com.example.aispeakingbuddy.container.LoginResponse
import com.example.aispeakingbuddy.repository.AuthRepository

class LoginViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    fun login(email: String, password: String): LiveData<LoginResponse> {
        val result = MutableLiveData<LoginResponse>()
        val request = LoginRequest(email, password)

        authRepository.login(request) { response ->
            result.postValue(response)
        }
        return result
    }
}
