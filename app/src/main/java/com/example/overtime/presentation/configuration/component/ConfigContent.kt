package com.example.overtime.presentation.configuration.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.overtime.presentation.configuration.viewmodel.ConfigViewModel

@Composable
fun ConfigContent(
    userName: String,
    userEmail: String,
    notificationsEnabled: Boolean,
    isDarkMode: Boolean,
    onNotificationToggle: (Boolean) -> Unit,
    onThemeToggle: (Boolean) -> Unit,
    navController: NavController,
    viewModel: ConfigViewModel
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Icono de logout arriba a la derecha
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { showLogoutDialog = true }) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = Color.Black
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                UserProfileCard(
                    userName = userName,
                    userEmail = userEmail,
                    navController = navController,
                    viewModel = viewModel
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
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Cerrar sesión") },
                text = { Text("¿Estás seguro que deseas cerrar sesión?") },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            viewModel.signOut(navController)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Cerrar sesión", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showLogoutDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
} 