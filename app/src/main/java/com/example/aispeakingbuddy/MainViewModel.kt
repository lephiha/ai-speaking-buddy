package com.example.aispeakingbuddy

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aispeakingbuddy.repository.AuthRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository()

    // Navigation
    private val _navigationDestination = MutableLiveData<String>()
    val navigationDestination: LiveData<String> = _navigationDestination

    // UI State
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun verifyToken(token: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val isValid = authRepository.verifyToken(token)

                if (isValid) {
                    // Token is valid, navigate to home
                    _navigationDestination.value = "home"
                } else {
                    // Token is invalid, navigate to login
                    clearTokenAndNavigateToLogin()
                }

            } catch (e: Exception) {
                // Network error or server error
                _errorMessage.value = "Không thể xác thực. Kiểm tra kết nối internet và thử lại."

            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun clearTokenAndNavigateToLogin() {
        // Clear invalid token
        val sharedPreferences = getApplication<Application>()
            .getSharedPreferences("user_prefs", Application.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            remove("accessToken")
            remove("user_name")
            remove("user_email")
            apply()
        }

        _navigationDestination.value = "login"
    }

    fun setNavigationDestination(destination: String) {
        _navigationDestination.value = destination
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
