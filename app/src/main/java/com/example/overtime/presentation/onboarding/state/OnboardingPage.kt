package com.example.overtime.presentation.onboarding.state

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.overtime.R

data class OnboardingPage(
    @DrawableRes val imageResId: Int,
    @StringRes val titleResId: Int,
    @StringRes val descriptionResId: Int,
)

val onboardingPages = listOf(
    OnboardingPage(
        imageResId = R.drawable.img_onboarding_page1,
        titleResId = R.string.onboarding_page1_title,
        descriptionResId = R.string.onboarding_page1_description
    ),
    OnboardingPage(
        imageResId = R.drawable.img_onboarding_page2,
        titleResId = R.string.onboarding_page2_title,
        descriptionResId = R.string.onboarding_page2_description
    ),
    OnboardingPage(
        imageResId = R.drawable.img_onboarding_page3,
        titleResId = R.string.onboarding_page3_title,
        descriptionResId = R.string.onboarding_page3_description
    ),
    OnboardingPage(
        imageResId = R.drawable.img_onboarding_page4,
        titleResId = R.string.onboarding_page4_title,
        descriptionResId = R.string.onboarding_page4_description
    ),
)
