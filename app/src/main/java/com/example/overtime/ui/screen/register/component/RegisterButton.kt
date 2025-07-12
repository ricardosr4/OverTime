package com.example.overtime.ui.screen.register.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.overtime.ui.component.ZetaButtonBasic
import com.example.overtime.ui.screen.register.viewModel.RegisterViewModel
import com.example.overtime.ui.theme.PrimaryColor

@Composable
fun RegisterButton(
    viewModel: RegisterViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    ZetaButtonBasic(
        onClick = {
            viewModel.createUser {
                navController.navigate("login_screen")
            }
        },
        backgroundColor = PrimaryColor,
        text = "Registrarse",
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    )
} 