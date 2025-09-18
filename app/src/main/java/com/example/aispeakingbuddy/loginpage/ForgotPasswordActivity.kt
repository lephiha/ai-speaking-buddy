package com.example.aispeakingbuddy.loginpage

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.aispeakingbuddy.R
import com.example.aispeakingbuddy.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)

        viewModel = ViewModelProvider(this)[ForgotPasswordViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupUI()
        setupObservers()
        setupClickListeners()
    }

    private fun setupUI() {
        window.statusBarColor = getColor(R.color.primary_color)

        // Pre-fill email if passed from login screen
        val email = intent.getStringExtra("email")
        if (!email.isNullOrEmpty()) {
            binding.etEmail.setText(email)
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnSendResetEmail.isEnabled = !isLoading
            binding.btnSendResetEmail.text = if (isLoading)
                "Đang gửi..." else "Gửi email khôi phục"
        }

        viewModel.resetEmailSent.observe(this) { sent ->
            if (sent) {
                showSuccessDialog()
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearErrorMessage()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSendResetEmail.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()

            if (validateEmail(email)) {
                viewModel.sendResetEmail(email)
            }
        }

        binding.tvBackToLogin.setOnClickListener {
            finish()
        }
    }

    private fun validateEmail(email: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.etEmail.error = "Vui lòng nhập email"
                binding.etEmail.requestFocus()
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.etEmail.error = "Email không hợp lệ"
                binding.etEmail.requestFocus()
                false
            }
            else -> {
                binding.etEmail.error = null
                true
            }
        }
    }

    private fun showSuccessDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Email đã được gửi")
            .setMessage("Chúng tôi đã gửi hướng dẫn khôi phục mật khẩu đến email của bạn. Vui lòng kiểm tra hộp thư và làm theo hướng dẫn.")
            .setPositiveButton("Đã hiểu") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}