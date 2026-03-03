package com.example.overtime.presentation.register.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.overtime.R
import com.example.overtime.presentation.login.components.ZetaOutlinedTextField
import com.example.overtime.presentation.login.components.ZetaSpaceHeight
import com.example.overtime.presentation.register.state.RegisterState
import com.example.overtime.presentation.register.viewModel.RegisterViewModel

@Composable
fun RegisterForm(
    registerState: RegisterState,
    viewModel: RegisterViewModel
) {
    ZetaOutlinedTextField(
        value = registerState.name,
        onValueChange = { viewModel.onNameChanged(it) },
        label = stringResource(R.string.config_user_name_label),
        keyboardType = KeyboardType.Text,
        leadingIcon = painterResource(id = R.drawable.icon_person)
    )
    ZetaSpaceHeight()

    ZetaOutlinedTextField(
        value = registerState.email,
        onValueChange = { viewModel.onEmailChanged(it) },
        label = stringResource(R.string.email),
        keyboardType = KeyboardType.Email,
        leadingIcon = painterResource(id = R.drawable.icon_email)
    )
    ZetaSpaceHeight()

    ZetaOutlinedTextField(
        value = registerState.password,
        onValueChange = { viewModel.onPasswordChanged(it) },
        label = stringResource(R.string.password),
        keyboardType = KeyboardType.Password,
        leadingIcon = painterResource(id = R.drawable.icon_password),
        isPassword = true,
        isPasswordVisible = registerState.isPasswordVisible,
        onVisibilityToggle = { viewModel.onPasswordVisibilityChanged() }
    )
    ZetaSpaceHeight(40.dp)
}