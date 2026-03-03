package com.example.overtime.presentation.login.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.overtime.R
import com.example.overtime.presentation.components.LoadingOverlay
import com.example.overtime.presentation.login.components.StandardButton
import com.example.overtime.presentation.login.components.ZetaAlertDialog
import com.example.overtime.presentation.login.components.ZetaImageLogo
import com.example.overtime.presentation.login.components.ZetaOutlinedTextField
import com.example.overtime.presentation.login.components.ZetaSpaceHeight
import com.example.overtime.presentation.login.components.ZetaText
import com.example.overtime.presentation.login.components.ZetaTextLink
import com.example.overtime.presentation.login.state.AlertType
import com.example.overtime.presentation.login.viewModel.LoginViewModel
import com.example.overtime.utils.getGoogleAccountFromIntent
import com.example.overtime.utils.getGoogleSignInErrorCode
import com.example.overtime.utils.getGoogleSignInIntent


@Composable
fun LoginScreen(navController: NavController) {

    val viewModel: LoginViewModel = hiltViewModel()
    val loginState by viewModel.loginState
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val account = getGoogleAccountFromIntent(result.data)
            val idToken = account?.idToken
            if (idToken != null) {
                isLoading = true
                viewModel.loginWithGoogle(
                    idToken,
                    onSuccess = {
                        isLoading = false
                        navController.navigate("home_screen")
                    },
                    onError = { errorMsg ->
                        isLoading = false
                        Toast.makeText(
                            context,
                            context.getString(R.string.login_error_generic, errorMsg),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            } else {
                val errorCode = getGoogleSignInErrorCode(result.data)
                val errorMsg = when (errorCode) {
                    10 -> context.getString(R.string.login_error_google_config)
                    12500 -> context.getString(R.string.login_error_google_failed)
                    12501 -> context.getString(R.string.login_error_google_canceled)
                    7 -> context.getString(R.string.login_error_network)
                    else -> context.getString(R.string.login_error_google_code, errorCode)
                }
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            }
        }

    LaunchedEffect(loginState) {
        if (loginState.isSuccess) {
            Toast.makeText(
                context,
                context.getString(R.string.login_success),
                Toast.LENGTH_SHORT
            ).show()
            viewModel.clearMessages()
        }

        loginState.errorMessage?.let {
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
                    text = stringResource(R.string.login),
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
                    label = stringResource(R.string.login_email_label),
                    keyboardType = KeyboardType.Email,
                    leadingIcon = painterResource(id = R.drawable.icon_email),

                    )
                ZetaSpaceHeight()
                ZetaOutlinedTextField(
                    value = loginState.password,
                    onValueChange = { viewModel.onPasswordChanged(it) },
                    label = stringResource(R.string.login_password_label),
                    keyboardType = KeyboardType.Password,
                    leadingIcon = painterResource(id = R.drawable.icon_password),
                    isPassword = true,
                    isPasswordVisible = loginState.isPasswordVisible,
                    onVisibilityToggle = { viewModel.onPasswordVisibilityChanged() }
                )
                ZetaSpaceHeight(40.dp)
                ZetaTextLink(
                    text = stringResource(R.string.login_no_account),
                    linkColor = MaterialTheme.colorScheme.primary,
                    textLink = stringResource(R.string.login_register_here),
                    onClick = { navController.navigate("register_screen") },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                ZetaSpaceHeight(20.dp)
                ZetaTextLink(
                    text = stringResource(R.string.login_recover_password),
                    linkColor = MaterialTheme.colorScheme.primary,
                    textLink = stringResource(R.string.login_here),
                    onClick = { viewModel.resetPassword(loginState.email) {} },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StandardButton(
                    onClick = {
                        viewModel.login(
                            email = loginState.email,
                            password = loginState.password
                        ) {
                            navController.navigate("home_screen")
                        }
                    },
                    text = stringResource(R.string.login),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loginState.isLoading
                )
                Spacer(modifier = Modifier.height(12.dp))
                StandardButton(
                    onClick = { launcher.launch(getGoogleSignInIntent(context)) },
                    text = stringResource(id = R.string.login_with_google),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loginState.isLoading
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
            if (loginState.showAlert) {
                val alertMessage = when (loginState.errorType) {
                    is AlertType.EmptyField ->
                        context.getString(R.string.login_error_fields_empty)
                    is AlertType.InvalidCredentials ->
                        context.getString(R.string.login_error_invalid_credentials)
                    is AlertType.ResetPasswordSuccess ->
                        context.getString(R.string.login_error_reset_success)
                    is AlertType.ResetPasswordEmptyField ->
                        context.getString(R.string.login_error_reset_email_empty)
                    is AlertType.ResetPasswordInvalidEmail ->
                        context.getString(R.string.login_error_reset_email_invalid)
                    else ->
                        context.getString(R.string.common_error_unexpected)
                }
                ZetaAlertDialog(
                    title = stringResource(R.string.common_alert_title),
                    message = alertMessage,
                    confirmText = stringResource(R.string.common_ok),
                    onConfirmClick = { viewModel.closeAlert() }
                )
            }
        }
        if (loginState.isLoading) {
            LoadingOverlay(modifier = Modifier.zIndex(1f))
        }
    }
}