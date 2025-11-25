package com.example.overtime.presentation.configuration.component

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.overtime.core.notifications.WeeklyOvertimeScheduler
import com.example.overtime.presentation.configuration.viewmodel.ConfigViewModel
import com.google.firebase.BuildConfig


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
    val context = LocalContext.current

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
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

                item {
                    Button(
                        onClick = { WeeklyOvertimeScheduler.scheduleInMinutes(context, 1) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Probar notificación en 1 min",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

            item {
                UserProfileCard(
                    userName = userName,
                    userEmail = userEmail,
                    navController = navController,
                    viewModel = viewModel
                )
            }
            item {
                // Permission-aware toggle for Android 13+
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { granted ->
                    onNotificationToggle(granted)
                }

                val requestOrToggle: (Boolean) -> Unit = { checked ->
                    if (checked) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            onNotificationToggle(true)
                        }
                    } else {
                        onNotificationToggle(false)
                    }
                }

                // Auto-solicitar permiso si el usuario ya tiene ON pero el permiso falta (tras reinstalar)
                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && notificationsEnabled) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                NotificationSettingsCard(
                    notificationsEnabled = notificationsEnabled,
                    onNotificationToggle = requestOrToggle
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
                            viewModel.signOut(navController, context)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Cerrar sesión", color = MaterialTheme.colorScheme.onPrimary)
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