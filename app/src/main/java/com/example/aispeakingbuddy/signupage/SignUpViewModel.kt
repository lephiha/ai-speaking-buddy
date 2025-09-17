package com.example.aispeakingbuddy.signuppage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aispeakingbuddy.container.SignUpResponse
import com.example.aispeakingbuddy.repository.AuthRepository

class SignUpViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    fun register(fullname: String, email: String, password: String): LiveData<SignUpResponse> {
        val result = MutableLiveData<SignUpResponse>()
        authRepository.register(fullname, email, password) { response ->
            result.postValue(response)
        }
        return result
    }
}
