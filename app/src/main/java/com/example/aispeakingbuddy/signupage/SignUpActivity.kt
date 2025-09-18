package com.example.aispeakingbuddy.signuppage

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.aispeakingbuddy.databinding.ActivitySignupBinding
import com.example.aispeakingbuddy.signupage.PrivacyPolicyActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val signUpViewModel: SignUpViewModel by viewModels()

    companion object {
        private const val PRIVACY_POLICY_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupObservers()
        setupClickListeners()
        setupTextWatchers()
    }

    private fun setupUI() {
        // Set status bar color
        window.statusBarColor = getColor(android.R.color.transparent)

        // Initially disable sign up button
        updateSignUpButton()
    }

    private fun setupObservers() {
        // Observe registration result
        signUpViewModel.registrationResult.observe(this) { response ->
            response?.let {
                if (it.status == "success") {
                    Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập", Toast.LENGTH_SHORT).show()
                    finish() // Return to LoginActivity
                } else {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observe loading state
        signUpViewModel.isLoading.observe(this) { isLoading ->
            binding.btnSignUp.isEnabled = !isLoading && isFormValid()
            binding.btnSignUp.text = if (isLoading) "Đang đăng ký..." else "Tạo tài khoản"

            // Disable other inputs during loading
            binding.etFullName.isEnabled = !isLoading
            binding.etEmail.isEnabled = !isLoading
            binding.etPassword.isEnabled = !isLoading
            binding.etConfirmPassword.isEnabled = !isLoading
            binding.cbTerms.isEnabled = !isLoading
        }

        // Observe validation errors
        signUpViewModel.validationError.observe(this) { error ->
            error?.let {
                when (it.field) {
                    "fullname" -> {
                        binding.etFullName.error = it.message
                        binding.etFullName.requestFocus()
                    }
                    "email" -> {
                        binding.etEmail.error = it.message
                        binding.etEmail.requestFocus()
                    }
                    "password" -> {
                        binding.etPassword.error = it.message
                        binding.etPassword.requestFocus()
                    }
                    "confirm_password" -> {
                        binding.etConfirmPassword.error = it.message
                        binding.etConfirmPassword.requestFocus()
                    }
                    "terms" -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        // Back button
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // Sign Up button
        binding.btnSignUp.setOnClickListener {
            if (validateForm()) {
                val fullname = binding.etFullName.text.toString().trim()
                val email = binding.etEmail.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()
                val confirmPassword = binding.etConfirmPassword.text.toString().trim()
                val termsAccepted = binding.cbTerms.isChecked

                signUpViewModel.register(fullname, email, password, confirmPassword, termsAccepted)
            }
        }

        // Google Sign Up button
        binding.btnGoogleSignUp.setOnClickListener {
            signUpViewModel.signUpWithGoogle()
        }

        // Sign In link
        binding.tvSignIn.setOnClickListener {
            finish() // Return to LoginActivity
        }

        // Terms checkbox
        binding.cbTerms.setOnCheckedChangeListener { _, isChecked ->
            updateSignUpButton()

            // If user unchecks, show privacy policy option
            if (!isChecked) {
                showPrivacyPolicyOption()
            }
        }
    }

    private fun setupTextWatchers() {
        // Real-time validation while typing
        binding.etFullName.addTextChangedListener {
            if (it != null && it.isNotEmpty()) {
                binding.etFullName.error = null
            }
            updateSignUpButton()
        }

        binding.etEmail.addTextChangedListener {
            if (it != null && it.isNotEmpty()) {
                binding.etEmail.error = null
                // Validate email format in real-time
                val email = it.toString().trim()
                if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.etEmail.error = "Email không hợp lệ"
                }
            }
            updateSignUpButton()
        }

        binding.etPassword.addTextChangedListener {
            if (it != null && it.length >= 6) {
                binding.etPassword.error = null
            }
            validatePasswordMatch()
            updateSignUpButton()
        }

        binding.etConfirmPassword.addTextChangedListener {
            validatePasswordMatch()
            updateSignUpButton()
        }
    }

    private fun validatePasswordMatch() {
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (confirmPassword.isNotEmpty() && password != confirmPassword) {
            binding.etConfirmPassword.error = "Mật khẩu nhập lại không khớp"
        } else if (confirmPassword.isNotEmpty() && password == confirmPassword) {
            binding.etConfirmPassword.error = null
        }
    }

    private fun updateSignUpButton() {
        val isFormValid = isFormValid()
        binding.btnSignUp.isEnabled = isFormValid && signUpViewModel.isLoading.value != true
        binding.btnSignUp.alpha = if (isFormValid && signUpViewModel.isLoading.value != true) 1.0f else 0.6f
    }

    private fun isFormValid(): Boolean {
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        val isTermsChecked = binding.cbTerms.isChecked

        return fullName.isNotEmpty() &&
                fullName.length >= 2 &&
                email.isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.length >= 6 &&
                password == confirmPassword &&
                isTermsChecked
    }

    private fun validateForm(): Boolean {
        clearErrors()

        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        val isTermsChecked = binding.cbTerms.isChecked

        var isValid = true

        // Validate full name
        if (fullName.isEmpty()) {
            binding.etFullName.error = "Vui lòng nhập họ tên"
            if (isValid) binding.etFullName.requestFocus()
            isValid = false
        } else if (fullName.length < 2) {
            binding.etFullName.error = "Họ tên phải có ít nhất 2 ký tự"
            if (isValid) binding.etFullName.requestFocus()
            isValid = false
        }

        // Validate email
        if (email.isEmpty()) {
            binding.etEmail.error = "Vui lòng nhập email"
            if (isValid) binding.etEmail.requestFocus()
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Email không hợp lệ"
            if (isValid) binding.etEmail.requestFocus()
            isValid = false
        }

        // Validate password
        if (password.isEmpty()) {
            binding.etPassword.error = "Vui lòng nhập mật khẩu"
            if (isValid) binding.etPassword.requestFocus()
            isValid = false
        } else if (password.length < 6) {
            binding.etPassword.error = "Mật khẩu phải có ít nhất 6 ký tự"
            if (isValid) binding.etPassword.requestFocus()
            isValid = false
        }

        // Validate confirm password
        if (confirmPassword.isEmpty()) {
            binding.etConfirmPassword.error = "Vui lòng nhập lại mật khẩu"
            if (isValid) binding.etConfirmPassword.requestFocus()
            isValid = false
        } else if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Mật khẩu nhập lại không khớp"
            if (isValid) binding.etConfirmPassword.requestFocus()
            isValid = false
        }

        // Validate terms
        if (!isTermsChecked) {
            Toast.makeText(this, "Bạn phải đồng ý với điều khoản và chính sách bảo mật", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun clearErrors() {
        binding.etFullName.error = null
        binding.etEmail.error = null
        binding.etPassword.error = null
        binding.etConfirmPassword.error = null
    }

    private fun showPrivacyPolicyOption() {
        val intent = Intent(this, PrivacyPolicyActivity::class.java)
        startActivityForResult(intent, PRIVACY_POLICY_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PRIVACY_POLICY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // User accepted privacy policy
                binding.cbTerms.isChecked = true
                updateSignUpButton()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}