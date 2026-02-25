package com.example.overtime.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import com.example.overtime.core.prefs.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val isOnboardingCompleted: Boolean
        get() = preferencesManager.isOnboardingCompleted()

    fun completeOnboarding() {
        preferencesManager.setOnboardingCompleted(true)
    }
}
