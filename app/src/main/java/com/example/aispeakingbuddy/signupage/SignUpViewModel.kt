package com.example.aispeakingbuddy.signuppage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aispeakingbuddy.repository.AuthRepository
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _registrationResult = MutableLiveData<RegistrationResponse?>()
    val registrationResult: LiveData<RegistrationResponse?> = _registrationResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _validationError = MutableLiveData<ValidationError?>()
    val validationError: LiveData<ValidationError?> = _validationError

    fun register(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String,
        termsAccepted: Boolean
    ) {
        // Clear previous results
        _registrationResult.value = null
        _validationError.value = null

        // Validate inputs
        if (!validateInputs(fullName, email, password, confirmPassword, termsAccepted)) {
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = authRepository.register(fullName, email, password)

                // Set the registration result properly
                _registrationResult.value = response

            } catch (e: Exception) {
                _registrationResult.value = RegistrationResponse(
                    status = "error",
                    message = "Đăng ký thất bại: ${e.message}"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signUpWithGoogle() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // TODO: Implement Google Sign Up
                // val response = authRepository.signUpWithGoogle()

                // For demo
                kotlinx.coroutines.delay(2000)
                _registrationResult.value = RegistrationResponse(
                    status = "info",
                    message = "Tính năng Google Sign Up sắp ra mắt!"
                )

            } catch (e: Exception) {
                _registrationResult.value = RegistrationResponse(
                    status = "error",
                    message = "Google Sign Up thất bại: ${e.message}"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun validateInputs(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String,
        termsAccepted: Boolean
    ): Boolean {

        when {
            fullName.isEmpty() -> {
                _validationError.value = ValidationError("fullname", "Vui lòng nhập họ tên")
                return false
            }
            fullName.length < 2 -> {
                _validationError.value = ValidationError("fullname", "Họ tên phải có ít nhất 2 ký tự")
                return false
            }
            email.isEmpty() -> {
                _validationError.value = ValidationError("email", "Vui lòng nhập email")
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _validationError.value = ValidationError("email", "Email không hợp lệ")
                return false
            }
            password.isEmpty() -> {
                _validationError.value = ValidationError("password", "Vui lòng nhập mật khẩu")
                return false
            }
            password.length < 6 -> {
                _validationError.value = ValidationError("password", "Mật khẩu phải có ít nhất 6 ký tự")
                return false
            }
            password != confirmPassword -> {
                _validationError.value = ValidationError("confirm_password", "Mật khẩu nhập lại không khớp")
                return false
            }
            !termsAccepted -> {
                _validationError.value = ValidationError("terms", "Bạn phải đồng ý với điều khoản và chính sách bảo mật")
                return false
            }
        }

        return true
    }
}

// Data classes
data class RegistrationResponse(
    val status: String,
    val message: String,
    val data: Any? = null
)

data class ValidationError(
    val field: String,
    val message: String
)