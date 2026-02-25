package com.example.overtime.presentation.onboarding.state

import androidx.annotation.DrawableRes
import com.example.overtime.R

data class OnboardingPage(
    @DrawableRes val imageResId: Int,
    val title: String,
    val description: String,
)

val onboardingPages = listOf(
    OnboardingPage(
        imageResId = R.drawable.img_onboarding_page1,
        title = "Bienvenido a\nOver Time",
        description = "La forma más sencilla de gestionar y calcular tus horas extras con precisión."
    ),
    OnboardingPage(
        imageResId = R.drawable.img_onboarding_page2,
        title = "Registra tus Horas",
        description = "Añade fácilmente tus jornadas adicionales indicando la fecha, porcentaje y cantidad de horas correspondientes."
    ),
    OnboardingPage(
        imageResId = R.drawable.img_onboarding_page3,
        title = "Reportes Detallados",
        description = "Genera informes en PDF de tus horas extras trabajadas cada mes de forma automática."
    ),
    OnboardingPage(
        imageResId = R.drawable.img_onboarding_page4,
        title = "¡Todo Listo!",
        description = "Empieza a tomar el control de tu tiempo y tus ingresos ahora mismo."
    ),
)
