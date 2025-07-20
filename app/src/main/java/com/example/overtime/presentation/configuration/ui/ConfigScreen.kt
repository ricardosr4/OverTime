package com.example.overtime.presentation.configuration.ui

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.overtime.presentation.configuration.viewmodel.ConfigViewModel
import com.example.overtime.presentation.configuration.component.ConfigContent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    navController: NavController,
    viewModel: ConfigViewModel = viewModel()
) {
    val userInfo by viewModel.userInfo.collectAsState()
    val (userName, userEmail) = userInfo

    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
    }

    Scaffold(
        content = {
            ConfigContent(
                userName = userName,
                userEmail = userEmail,
                notificationsEnabled = viewModel.notificationsEnabled,
                isDarkMode = viewModel.isDarkMode,
                onNotificationToggle = { viewModel.toggleNotifications() },
                onThemeToggle = { viewModel.toggleTheme() },
                navController = navController,
                viewModel = viewModel
            )
        }
    )
}
