package com.example.overtime.ui.screen.configuration.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ConfigContent(
    userName: String,
    userEmail: String,
    notificationsEnabled: Boolean,
    isDarkMode: Boolean,
    onNotificationToggle: (Boolean) -> Unit,
    onThemeToggle: (Boolean) -> Unit,
    onLogout: () -> Unit,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            UserProfileCard(
                userName = userName,
                userEmail = userEmail
            )
        }
        
        item {
            NotificationSettingsCard(
                notificationsEnabled = notificationsEnabled,
                onNotificationToggle = onNotificationToggle
            )
        }
        
        item {
            ThemeSettingsCard(
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle
            )
        }
        
        item {
            AppInfoCard()
        }
        
        item {
            LogoutButton(
                onLogout = onLogout
            )
        }
    }
} 