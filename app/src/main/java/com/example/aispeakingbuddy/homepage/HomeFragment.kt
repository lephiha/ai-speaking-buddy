package com.example.aispeakingbuddy.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.aispeakingbuddy.R
import com.example.aispeakingbuddy.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupClickListeners()

        // Load user data when fragment is created
        viewModel.loadUserData()
    }

    private fun setupObservers() {
        // Observe user data changes
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.tvWelcome.text = "Welcome back, $name! 👋"
        }

        viewModel.streakDays.observe(viewLifecycleOwner) { streak ->
            viewModel.userLevel.observe(viewLifecycleOwner) { level ->
                binding.tvStreak.text = "🔥 $streak day streak • Level: $level"
            }
        }

        viewModel.lessonProgress.observe(viewLifecycleOwner) { progress ->
            binding.progressLesson.progress = progress
            val completedExercises = progress * 5 / 100
            binding.tvLessonProgress.text = "$completedExercises/5 exercises completed"
        }

        viewModel.currentLessonTitle.observe(viewLifecycleOwner) { title ->
            binding.tvLessonTitle.text = title
        }

        // Observe navigation events
        viewModel.navigationEvent.observe(viewLifecycleOwner) { event ->
            handleNavigationEvent(event)
        }

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Show/hide loading indicator
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe error messages
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorMessage()
            }
        }
    }

    private fun setupClickListeners() {
        // Profile click
        binding.ivProfile.setOnClickListener {
            viewModel.onProfileClicked()
        }

        // Notification click
        binding.ivNotification.setOnClickListener {
            viewModel.onNotificationClicked()
        }

        // Continue Learning button
        binding.btnContinueLearning.setOnClickListener {
            viewModel.onContinueLearningClicked()
        }

        // Feature cards click listeners
        binding.cardAIChat.setOnClickListener {
            viewModel.onAIChatClicked()
        }

        binding.cardDailyQuiz.setOnClickListener {
            viewModel.onDailyQuizClicked()
        }

        binding.cardFlashcards.setOnClickListener {
            viewModel.onFlashcardsClicked()
        }

        binding.cardProgress.setOnClickListener {
            viewModel.onProgressClicked()
        }
    }

    private fun handleNavigationEvent(event: String?) {
        when (event) {
            "chat" -> {
                findNavController().navigate(R.id.action_home_to_chat)
            }
            "quiz" -> {
                findNavController().navigate(R.id.action_home_to_quiz)
            }
            "flashcards" -> {
                findNavController().navigate(R.id.action_home_to_flashcards)
            }
            "progress" -> {
                findNavController().navigate(R.id.action_home_to_profile)
            }
            "continue_lesson" -> {
                findNavController().navigate(R.id.action_home_to_lesson)
            }
            "profile" -> {
                findNavController().navigate(R.id.action_home_to_profile)
            }
            "notifications" -> {
                Toast.makeText(context, "Opening Notifications...", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.clearNavigationEvent()
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning to home
        viewModel.refreshUserData()
    }

    override fun onPause() {
        super.onPause()
        // Save current session data
        viewModel.saveSessionData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}