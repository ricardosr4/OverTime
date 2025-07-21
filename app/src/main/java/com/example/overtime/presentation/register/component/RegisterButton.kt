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
    modifier: Modifier = Modifier
) {
    StandardButton(
        onClick = {
            viewModel.createUser {
                navController.navigate("login_screen")
            }
        },
        text = "Registrarse",
        modifier = modifier
    )
} 