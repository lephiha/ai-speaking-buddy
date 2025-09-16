package com.example.aispeakingbuddy.configuration

object Constant {
    // Base API
    fun APP_PATH(): String {
        return "http://192.168.50.128:8080/ai_speaking_buddy_api/index.php?route="
    }

    // App name
    fun APP_NAME(): String {
        return "AI Speaking Buddy"
    }

    private var accessToken: String? = null

    fun setAccessToken(token: String) {
        accessToken = token
    }

    fun getAccessToken(): String? {
        return accessToken
    }
}
