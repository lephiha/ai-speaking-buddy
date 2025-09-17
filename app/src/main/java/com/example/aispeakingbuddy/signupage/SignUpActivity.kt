package com.example.aispeakingbuddy.signuppage

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.aispeakingbuddy.databinding.ActivitySignupBinding
import com.example.aispeakingbuddy.loginpage.LoginActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val fullname = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            signUpViewModel.register(fullname, email, password).observe(this) { response ->
                if (response.status == "success") {
                    Toast.makeText(this, "Register success! Please login", Toast.LENGTH_SHORT).show()
                    finish() // quay lại LoginActivity
                } else {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvSignIn.setOnClickListener {
            finish() // quay lại LoginActivity
        }
    }
}
