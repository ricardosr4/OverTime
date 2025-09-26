package com.example.overtime.presentation.register.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.overtime.presentation.login.components.ZetaTextLink

@Composable
fun RegisterLinks(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    ZetaTextLink(
        text = "¿Ya tienes una cuenta?",
        linkColor = MaterialTheme.colorScheme.primary,
        textLink = "Inicia Sesión",
        onClick = { navController.navigate("login_screen") },
        modifier = modifier
    )
} 