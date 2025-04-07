package com.example.overtime.ui.screen.register.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.overtime.R
import com.example.overtime.ui.component.ZetaAlertDialog
import com.example.overtime.ui.component.ZetaButtonBasic
import com.example.overtime.ui.component.ZetaImageLogo
import com.example.overtime.ui.component.ZetaOutlinedTextField
import com.example.overtime.ui.component.ZetaSpaceHeight
import com.example.overtime.ui.component.ZetaText
import com.example.overtime.ui.component.ZetaTextLink
import com.example.overtime.ui.screen.register.state.AlertTypeRegister
import com.example.overtime.ui.screen.register.viewModel.RegisterViewModel
import com.example.overtime.ui.theme.PrimaryColor

@Composable
fun RegisterScreen(navController: NavController) {

    val viewModel: RegisterViewModel = viewModel()
    val registerState by viewModel.registerState
    val context = LocalContext.current

    LaunchedEffect(registerState) {
        if (registerState.isSuccess) {
            Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }

        registerState.errorMessage?.let {
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
                text = "Registrase",
                fontSize = 30.sp,
                maxLines = 1,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.Start)

            )
            ZetaSpaceHeight(20.dp)

            ZetaOutlinedTextField(
                value = registerState.name,
                onValueChange = { viewModel.onNameChanged(it)},
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
                onValueChange = { viewModel.onPasswordChanged(it)},
                label = "Paswword",
                keyboardType = KeyboardType.Password,
                leadingIcon = painterResource(id = R.drawable.icon_password),
                isPassword = true,
                isPasswordVisible = registerState.isPasswordVisible,
                onVisibilityToggle = { viewModel.onPasswordVisibilityChanged() }
            )
            ZetaSpaceHeight(40.dp)
            ZetaTextLink(
                text = "¿Ya tienes una cuenta?", linkColor = PrimaryColor,
                textLink = "Inicia Sesión",
                onClick = { navController.navigate("login_screen") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )


        }
        ZetaButtonBasic(
            onClick = {
                viewModel.createUser {
                    navController.navigate("login_screen")
                }
            },
            backgroundColor = PrimaryColor,
            text = "Registrarse",
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(20.dp)
        )
        if (registerState.showAlert) {
            val alertMessage = when (registerState.errorType) {
                is AlertTypeRegister.EmptyField -> "Los campos no pueden estar vacíos."
                is AlertTypeRegister.InvalidEmail -> "El correo electrónico no es válido."
                is AlertTypeRegister.InvalidPassword -> "La contraseña debe tener al menos 6 caracteres."
                is AlertTypeRegister.UnknownError -> registerState.errorMessage ?: "Ha ocurrido un error inesperado."
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
