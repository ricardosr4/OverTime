package com.example.overtime.ui.screen.register.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.overtime.ui.component.ZetaTextLink
import com.example.overtime.ui.theme.PrimaryColor

@Composable
fun RegisterLinks(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    ZetaTextLink(
        text = "¿Ya tienes una cuenta?",
        linkColor = PrimaryColor,
        textLink = "Inicia Sesión",
        onClick = { navController.navigate("login_screen") },
        modifier = modifier
    )
} 