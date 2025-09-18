package com.example.aispeakingbuddy.loginpage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aispeakingbuddy.repository.AuthRepository
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _resetEmailSent = MutableLiveData<Boolean>()
    val resetEmailSent: LiveData<Boolean> = _resetEmailSent

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun sendResetEmail(email: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val success = authRepository.sendPasswordResetEmail(email)

                if (success) {
                    _resetEmailSent.value = true
                } else {
                    _errorMessage.value = "Email không tồn tại trong hệ thống"
                }

            } catch (e: Exception) {
                _errorMessage.value = "Không thể gửi email khôi phục. Vui lòng thử lại sau."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}