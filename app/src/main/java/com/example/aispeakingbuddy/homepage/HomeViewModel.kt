package com.example.aispeakingbuddy.homepage

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aispeakingbuddy.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application)
    private val sharedPreferences = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // User Data
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _streakDays = MutableLiveData<Int>()
    val streakDays: LiveData<Int> = _streakDays

    private val _userLevel = MutableLiveData<String>()
    val userLevel: LiveData<String> = _userLevel

    private val _lessonProgress = MutableLiveData<Int>()
    val lessonProgress: LiveData<Int> = _lessonProgress

    private val _currentLessonTitle = MutableLiveData<String>()
    val currentLessonTitle: LiveData<String> = _currentLessonTitle

    // UI State
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Navigation Events
    private val _navigationEvent = MutableLiveData<String?>()
    val navigationEvent: LiveData<String?> = _navigationEvent

    init {
        loadUserData()
    }

    fun loadUserData() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // Load from local storage first
                loadLocalUserData()

                // Then sync with server
                syncUserDataFromServer()

            } catch (e: Exception) {
                _errorMessage.value = "Không thể tải dữ liệu người dùng: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadLocalUserData() {
        _userName.value = sharedPreferences.getString("user_name", "Student") ?: "Student"
        _streakDays.value = sharedPreferences.getInt("streak_days", 1)
        _userLevel.value = sharedPreferences.getString("user_level", "Beginner") ?: "Beginner"
        _lessonProgress.value = sharedPreferences.getInt("lesson_progress", 60)
        _currentLessonTitle.value = sharedPreferences.getString("current_lesson", "Greetings & Introductions")
            ?: "Greetings & Introductions"
    }

    private suspend fun syncUserDataFromServer() {
        try {
            val userData = userRepository.getUserData()

            // Update LiveData with server data
            _userName.value = userData.name
            _streakDays.value = userData.streakDays
            _userLevel.value = userData.level
            _lessonProgress.value = userData.lessonProgress
            _currentLessonTitle.value = userData.currentLesson

            // Save to local storage
            saveUserDataLocally(userData)

        } catch (e: Exception) {
            // If server sync fails, continue with local data
        }
    }

    private fun saveUserDataLocally(userData: UserData) {
        with(sharedPreferences.edit()) {
            putString("user_name", userData.name)
            putInt("streak_days", userData.streakDays)
            putString("user_level", userData.level)
            putInt("lesson_progress", userData.lessonProgress)
            putString("current_lesson", userData.currentLesson)
            putLong("last_sync", System.currentTimeMillis())
            apply()
        }
    }

    fun refreshUserData() {
        if (isLoading.value != true) {
            loadUserData()
        }
    }

    // Click Handlers
    fun onProfileClicked() {
        _navigationEvent.value = "profile"
        trackUserAction("profile_clicked")
    }

    fun onNotificationClicked() {
        _navigationEvent.value = "notifications"
        trackUserAction("notification_clicked")
    }

    fun onContinueLearningClicked() {
        _navigationEvent.value = "continue_lesson"
        trackUserAction("continue_lesson_clicked")
    }

    fun onAIChatClicked() {
        _navigationEvent.value = "chat"
        trackUserAction("ai_chat_clicked")
    }

    fun onDailyQuizClicked() {
        _navigationEvent.value = "quiz"
        trackUserAction("daily_quiz_clicked")
    }

    fun onFlashcardsClicked() {
        _navigationEvent.value = "flashcards"
        trackUserAction("flashcards_clicked")
    }

    fun onProgressClicked() {
        _navigationEvent.value = "progress"
        trackUserAction("progress_clicked")
    }

    // Helper Methods
    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun saveSessionData() {
        viewModelScope.launch {
            try {
                with(sharedPreferences.edit()) {
                    putLong("last_session", System.currentTimeMillis())
                    apply()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Không thể lưu dữ liệu phiên làm việc"
            }
        }
    }

    fun updateLessonProgress(newProgress: Int) {
        _lessonProgress.value = newProgress

        // Save locally
        with(sharedPreferences.edit()) {
            putInt("lesson_progress", newProgress)
            apply()
        }

        // Sync with server
        viewModelScope.launch {
            try {
                userRepository.updateProgress(newProgress)
            } catch (e: Exception) {
                // Server sync failed, but local data is saved
            }
        }
    }

    fun updateStreakDays() {
        val lastSessionDate = sharedPreferences.getLong("last_session_date", 0)
        val today = System.currentTimeMillis()
        val oneDayMillis = 24 * 60 * 60 * 1000

        if (today - lastSessionDate < oneDayMillis * 2) {
            // Increment streak
            val currentStreak = _streakDays.value ?: 1
            val newStreak = currentStreak + 1
            _streakDays.value = newStreak

            with(sharedPreferences.edit()) {
                putInt("streak_days", newStreak)
                putLong("last_session_date", today)
                apply()
            }
        } else {
            // Reset streak
            _streakDays.value = 1
            with(sharedPreferences.edit()) {
                putInt("streak_days", 1)
                putLong("last_session_date", today)
                apply()
            }
        }
    }

    private fun trackUserAction(action: String) {
        viewModelScope.launch {
            try {
                userRepository.trackUserAction(action)
            } catch (e: Exception) {
                // Analytics failure shouldn't affect UX
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Cleanup resources
    }
}
