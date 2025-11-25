package com.example.overtime.presentation.configuration.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.overtime.presentation.configuration.viewmodel.ConfigViewModel
import com.example.overtime.presentation.configuration.component.ConfigContent
import com.example.overtime.core.prefs.ThemeMode
import com.example.overtime.presentation.components.LoadingOverlay


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    navController: NavController,
    viewModel: ConfigViewModel = hiltViewModel()
) {
    val userInfo by viewModel.userInfo.collectAsState()
    val (userName, userEmail) = userInfo

    // Calcular isDarkMode en el composable
    val themeMode by viewModel.themeModeFlow.collectAsState()
    val isDarkMode = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    // RESTAURADO: getCurrentUser
    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
    }

    Scaffold(
        content = {
            Box(modifier = Modifier.fillMaxSize()) {


                ConfigContent(
                    userName = userName,
                    userEmail = userEmail,
                    notificationsEnabled = viewModel.notificationsEnabledFlow.collectAsState().value,
                    isDarkMode = isDarkMode,
                    onNotificationToggle = { enabled -> viewModel.setNotificationsEnabled(enabled) },
                    onThemeToggle = { viewModel.toggleTheme() },
                    navController = navController,
                    viewModel = viewModel
                )
                if (viewModel.isLoading.collectAsState().value) {
                    LoadingOverlay()
                }

            }
        }
    )
}