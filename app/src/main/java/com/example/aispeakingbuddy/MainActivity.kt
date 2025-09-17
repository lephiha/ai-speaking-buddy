package com.example.aispeakingbuddy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.aispeakingbuddy.databinding.ActivityMainBinding
import com.example.aispeakingbuddy.homepage.HomeActivity
import com.example.aispeakingbuddy.loginpage.LoginActivity
import com.example.aispeakingbuddy.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val splashDelay = 2000L // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setupUI()
        observeViewModel()
        startSplashSequence()
    }

    private fun setupUI() {
        // Hide status bar for splash effect
        window.statusBarColor = getColor(R.color.primary_color)

        // Set app info
        binding.tvAppName.text = "AI Speaking Buddy"
        binding.tvAppSubtitle.text = "Your personal English learning companion"

        // Start logo animation
        startLogoAnimation()
    }

    private fun startLogoAnimation() {
        // Fade in animation for logo
        binding.ivLogo.alpha = 0f
        binding.ivLogo.animate()
            .alpha(1f)
            .setDuration(1000)
            .start()

        // Fade in animation for text
        binding.tvAppName.alpha = 0f
        binding.tvAppSubtitle.alpha = 0f

        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvAppName.animate()
                .alpha(1f)
                .setDuration(800)
                .start()

            binding.tvAppSubtitle.animate()
                .alpha(1f)
                .setDuration(800)
                .setStartDelay(200)
                .start()
        }, 500)
    }

    private fun observeViewModel() {
        viewModel.navigationDestination.observe(this) { destination ->
            when (destination) {
                "login" -> navigateToLogin()
                "home" -> navigateToHome()
                "onboarding" -> navigateToOnboarding()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading)
                android.view.View.VISIBLE else android.view.View.GONE
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                // Show error dialog or toast
                showErrorDialog(it)
            }
        }
    }

    private fun startSplashSequence() {
        Handler(Looper.getMainLooper()).postDelayed({
            // Check authentication status after splash delay
            checkAuthenticationStatus()
        }, splashDelay)
    }

    private fun checkAuthenticationStatus() {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken != null) {
            // Token exists, verify with server
            viewModel.verifyToken(accessToken)
        } else {
            // No token, check if first time user
            val isFirstTime = sharedPreferences.getBoolean("is_first_time", true)
            if (isFirstTime) {
                viewModel.setNavigationDestination("onboarding")
            } else {
                viewModel.setNavigationDestination("login")
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun navigateToOnboarding() {
        // Navigate to onboarding flow if you have one
        // For now, navigate to login
        navigateToLogin()
    }

    private fun showErrorDialog(message: String) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Lỗi kết nối")
            .setMessage(message)
            .setPositiveButton("Thử lại") { _, _ ->
                checkAuthenticationStatus()
            }
            .setNegativeButton("Bỏ qua") { _, _ ->
                navigateToLogin()
            }
            .setCancelable(false)
            .show()
    }

    override fun onBackPressed() {
        // Disable back button on splash screen
        // Do nothing
    }
}