package com.example.overtime.presentation.register.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.overtime.presentation.login.components.StandardButton
import com.example.overtime.presentation.register.viewModel.RegisterViewModel

@Composable
fun RegisterButton(
    viewModel: RegisterViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    StandardButton(
        onClick = {
            viewModel.createUser {}
        },
        text = "Registrarse",
        modifier = modifier,
        enabled = enabled
    )
} 