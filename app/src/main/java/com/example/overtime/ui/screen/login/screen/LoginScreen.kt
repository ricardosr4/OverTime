package com.example.overtime.ui.screen.login.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.overtime.R
import com.example.overtime.ui.component.ZetaAlertDialog
import com.example.overtime.ui.component.ZetaButtonBasic
import com.example.overtime.ui.component.ZetaImageLogo
import com.example.overtime.ui.component.ZetaOutlinedTextField
import com.example.overtime.ui.component.ZetaSpaceHeight
import com.example.overtime.ui.component.ZetaText
import com.example.overtime.ui.component.ZetaTextLink
import com.example.overtime.ui.screen.login.state.AlertType
import com.example.overtime.ui.theme.PrimaryColor
import com.example.overtime.ui.screen.login.viewModel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController) {

    val viewModel: LoginViewModel = viewModel()
    val loginState by viewModel.loginState
    val context = LocalContext.current

    LaunchedEffect(loginState) {
        if (loginState.isSuccess) {
            Toast.makeText(context, "¡Ingreso exitoso!", Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }

        loginState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
            .background(Color.White)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            ZetaImageLogo(
                image = painterResource(R.drawable.img_over_time),
                width = 150.dp,
                height = 150.dp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp)
            )
            ZetaSpaceHeight(30.dp)
            ZetaText(
                text = "Login",
                fontSize = 30.sp,
                maxLines = 1,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.Start)
            )
            ZetaSpaceHeight(20.dp)
            ZetaOutlinedTextField(
                value = loginState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = "Email",
                leadingIcon = painterResource(id = R.drawable.icon_email),

            )
            ZetaSpaceHeight()
            ZetaOutlinedTextField(
                value = loginState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = "Paswword",
                leadingIcon = painterResource(id = R.drawable.icon_password),
                isPassword = true,
                isPasswordVisible = loginState.isPasswordVisible,
                onVisibilityToggle = { viewModel.onPasswordVisibilityChanged() }
            )
            ZetaSpaceHeight(40.dp)
            ZetaTextLink(
                text = "¿No tienes cuenta?", linkColor = PrimaryColor,
                textLink = "Registrate aqui!!",
                onClick = { navController.navigate("register_screen") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            ZetaSpaceHeight(20.dp)
            ZetaTextLink(
                text = stringResource(R.string.recuperar_contraseña), linkColor = PrimaryColor,
                textLink = stringResource(R.string.aqui),
                onClick = { viewModel.resetPassword(loginState.email) {} },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
        ZetaButtonBasic(
            onClick = {
                viewModel.login(
                    email = loginState.email,
                    password = loginState.password
                ) {
                    navController.navigate("home_screen")
                }
            },
            backgroundColor = PrimaryColor,
            text = "Login", color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(20.dp)
        )
        if (loginState.showAlert) {
            val alertMessage = when (loginState.errorType) {
                is AlertType.EmptyField -> "Los campos no pueden estar vacíos."
                is AlertType.InvalidCredentials -> "Usuario y/o contraseña incorrectos."
                is AlertType.ResetPasswordSuccess -> "Se ha enviado un correo para restablecer la contraseña."
                is AlertType.ResetPasswordEmptyField -> "El campo de correo no puede estar vacío."
                is AlertType.ResetPasswordInvalidEmail -> "El correo electrónico no es válido."
                else -> "Ha ocurrido un error inesperado."
            }
            ZetaAlertDialog(
                title = "Alerta",
                message = alertMessage,
                confirmText = "Aceptar",
                onConfirmClick = { viewModel.closeAlert() }
            ) { }
        }
    }
}
