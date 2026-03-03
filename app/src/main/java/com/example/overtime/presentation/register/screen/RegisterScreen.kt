package com.example.overtime.presentation.register.screen

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.overtime.R
import com.example.overtime.presentation.components.LoadingOverlay
import com.example.overtime.presentation.login.components.ZetaAlertDialog
import com.example.overtime.presentation.login.components.ZetaImageLogo
import com.example.overtime.presentation.login.components.ZetaSpaceHeight
import com.example.overtime.presentation.login.components.ZetaText
import com.example.overtime.presentation.register.component.RegisterButton
import com.example.overtime.presentation.register.component.RegisterForm
import com.example.overtime.presentation.register.component.RegisterLinks
import com.example.overtime.presentation.register.state.AlertTypeRegister
import com.example.overtime.presentation.register.viewModel.RegisterViewModel
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(navController: NavController) {
    val viewModel: RegisterViewModel = hiltViewModel()
    val registerState by viewModel.registerState
    val context = LocalContext.current

    LaunchedEffect(registerState) {
        if (registerState.isSuccess) {
            Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
            delay(500)
            viewModel.clearMessages()
            navController.navigate("login_screen")
        }

        registerState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 30.dp)
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
                    text = stringResource(R.string.register),
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
                modifier = Modifier.align(Alignment.BottomCenter),
                enabled = !registerState.isLoading
            )

            if (registerState.showAlert) {
                val alertMessage = when (registerState.errorType) {
                    is AlertTypeRegister.EmptyField ->
                        stringResource(R.string.register_error_fields_empty)
                    is AlertTypeRegister.InvalidEmail ->
                        stringResource(R.string.register_error_invalid_email)
                    is AlertTypeRegister.InvalidPassword ->
                        stringResource(R.string.register_error_invalid_password)
                    is AlertTypeRegister.UnknownError ->
                        registerState.errorMessage ?: stringResource(R.string.common_error_unexpected)
                    else ->
                        stringResource(R.string.common_error_unexpected)
                }
                ZetaAlertDialog(
                    title = stringResource(R.string.common_alert_title),
                    message = alertMessage,
                    confirmText = stringResource(R.string.common_ok),
                    onConfirmClick = { viewModel.closeAlert() }
                )
            }
        }
        if (registerState.isLoading) {
            LoadingOverlay(modifier = Modifier.zIndex(1f))
        }
    }
}
