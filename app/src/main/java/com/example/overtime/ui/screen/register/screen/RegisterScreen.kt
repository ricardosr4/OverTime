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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.overtime.R
import com.example.overtime.ui.component.ZetaAlertDialog
import com.example.overtime.ui.component.ZetaImageLogo
import com.example.overtime.ui.component.ZetaSpaceHeight
import com.example.overtime.ui.component.ZetaText
import com.example.overtime.ui.screen.register.component.RegisterButton
import com.example.overtime.ui.screen.register.component.RegisterForm
import com.example.overtime.ui.screen.register.component.RegisterLinks
import com.example.overtime.ui.screen.register.state.AlertTypeRegister
import com.example.overtime.ui.screen.register.viewModel.RegisterViewModel

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
    ) {
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
                text = "Registrarse",
                fontSize = 30.sp,
                maxLines = 1,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.Start)
            )
            ZetaSpaceHeight(20.dp)

            RegisterForm(registerState, viewModel)
            RegisterLinks(
                navController = navController,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        RegisterButton(
            viewModel = viewModel,
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        if (registerState.showAlert) {
            val alertMessage = when (registerState.errorType) {
                is AlertTypeRegister.EmptyField -> "Los campos no pueden estar vacíos."
                is AlertTypeRegister.InvalidEmail -> "El correo electrónico no es válido."
                is AlertTypeRegister.InvalidPassword -> "La contraseña debe tener al menos 6 caracteres."
                is AlertTypeRegister.UnknownError -> registerState.errorMessage
                    ?: "Ha ocurrido un error inesperado."

                else -> "Ha ocurrido un error inesperado."
            }
            ZetaAlertDialog(
                title = "Alerta",
                message = alertMessage,
                confirmText = "Aceptar",
                onConfirmClick = { viewModel.closeAlert() }
            )
        }
    }
}
