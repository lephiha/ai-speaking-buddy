package com.example.aispeakingbuddy.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {

    suspend fun getUserData(): UserData {
        return withContext(Dispatchers.IO) {
            // TODO: Make API call to get user data
            // For now, return dummy data
            UserData(
                name = "John Doe",
                streakDays = 7,
                level = "Beginner",
                lessonProgress = 60,
                currentLesson = "Greetings & Introductions"
            )
        }
    }

    suspend fun updateProgress(progress: Int) {
        withContext(Dispatchers.IO) {
            // TODO: Make API call to update progress
        }
    }

    suspend fun trackUserAction(action: String) {
        withContext(Dispatchers.IO) {
            // TODO: Send analytics to server
        }
    }

    fun clearUserCache() {
        // Clear any cached user data
    }
}

// Data class for user information
data class UserData(
    val name: String,
    val streakDays: Int,
    val level: String,
    val lessonProgress: Int,
    val currentLesson: String
)