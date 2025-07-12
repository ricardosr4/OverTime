package com.example.overtime.ui.screen.register.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.overtime.R
import com.example.overtime.ui.component.ZetaOutlinedTextField
import com.example.overtime.ui.component.ZetaSpaceHeight
import com.example.overtime.ui.screen.register.state.RegisterState
import com.example.overtime.ui.screen.register.viewModel.RegisterViewModel

@Composable
fun RegisterForm(
    registerState: RegisterState,
    viewModel: RegisterViewModel
) {
    ZetaOutlinedTextField(
        value = registerState.name,
        onValueChange = { viewModel.onNameChanged(it) },
        label = "Nombre",
        keyboardType = KeyboardType.Text,
        leadingIcon = painterResource(id = R.drawable.icon_person)
    )
    ZetaSpaceHeight()
    
    ZetaOutlinedTextField(
        value = registerState.email,
        onValueChange = { viewModel.onEmailChanged(it) },
        label = "Email",
        keyboardType = KeyboardType.Email,
        leadingIcon = painterResource(id = R.drawable.icon_email)
    )
    ZetaSpaceHeight()
    
    ZetaOutlinedTextField(
        value = registerState.password,
        onValueChange = { viewModel.onPasswordChanged(it) },
        label = "Paswword",
        keyboardType = KeyboardType.Password,
        leadingIcon = painterResource(id = R.drawable.icon_password),
        isPassword = true,
        isPasswordVisible = registerState.isPasswordVisible,
        onVisibilityToggle = { viewModel.onPasswordVisibilityChanged() }
    )
    ZetaSpaceHeight(40.dp)
} 