package com.example.aispeakingbuddy.loginpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aispeakingbuddy.container.LoginRequest
import com.example.aispeakingbuddy.container.LoginResponse
import com.example.aispeakingbuddy.repository.AuthRepository

class LoginViewModel : ViewModel() {
    private val repository = AuthRepository()

    val loginResponse = MutableLiveData<LoginResponse>()
    val isLoading = MutableLiveData<Boolean>()

    fun login(email: String, password: String) {
        isLoading.value = true
        val request = LoginRequest(email, password)

        repository.login(request) { response ->
            loginResponse.postValue(response)
            isLoading.postValue(false)
        }
    }
}
