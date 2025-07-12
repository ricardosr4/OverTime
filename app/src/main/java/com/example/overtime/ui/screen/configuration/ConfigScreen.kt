package com.example.overtime.ui.screen.configuration

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.overtime.ui.screen.configuration.component.ConfigContent
import com.example.overtime.ui.screen.configuration.component.ConfigTopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    navController: NavController,
    viewModel: ConfigViewModel = viewModel()
) {
    val (userName, userEmail) = viewModel.getCurrentUser()

    Scaffold(
        topBar = {
            ConfigTopBar(viewModel, navController)
        },
        content = { paddingValues ->
            ConfigContent(
                userName = userName,
                userEmail = userEmail,
                notificationsEnabled = viewModel.notificationsEnabled,
                isDarkMode = viewModel.isDarkMode,
                onNotificationToggle = { viewModel.toggleNotifications() },
                onThemeToggle = { viewModel.toggleTheme() },
                onLogout = { viewModel.signOut(navController) },
                paddingValues = paddingValues
            )
        }
    )
}
