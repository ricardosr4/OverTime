package com.example.overtime.presentation.preLogin.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.overtime.R
import com.example.overtime.presentation.navigation.AppScreen
import com.example.overtime.presentation.login.components.StandardButton
import com.example.overtime.ui.theme.ButtonDisabled
import com.example.overtime.ui.theme.ButtonPrimaryText
import com.example.overtime.ui.theme.DividerColor
import com.example.overtime.ui.theme.TextPrimary
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.overtime.presentation.login.viewModel.LoginViewModel
import com.example.overtime.utils.getGoogleSignInIntent
import com.example.overtime.utils.getGoogleAccountFromIntent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.CircularProgressIndicator
import com.example.overtime.presentation.components.LoadingOverlay
import androidx.compose.ui.zIndex
import com.example.overtime.ui.theme.ButtonPrimary

@Composable
fun PreLoginScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = hiltViewModel()
    var isLoading by remember { mutableStateOf(false) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val account = getGoogleAccountFromIntent(result.data)
            val idToken = account?.idToken
            if (idToken != null) {
                isLoading = true
                viewModel.loginWithGoogle(idToken, onSuccess = {
                    isLoading = false
                    navController.navigate("home_screen")
                }, onError = {
                    isLoading = false
                    // Manejar error si quieres
                })
            }
        }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_over_time),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(320.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 10.dp,
                                bottomEnd = 10.dp
                            )
                        )
                )
                Spacer(modifier = Modifier.height(100.dp))
                // El resto del contenido principal aquí (si lo hay)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StandardButton(
                    onClick = { navController.navigate(AppScreen.LoginScreen.route) },
                    text = stringResource(R.string.login),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    enabled = !isLoading
                )
                Spacer(modifier = Modifier.height(10.dp))
                StandardButton(
                    onClick = { navController.navigate(AppScreen.RegisterScreen.route) },
                    text = stringResource(R.string.register),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    enabled = !isLoading
                )
                Spacer(modifier = Modifier.height(20.dp))
//                Button(
//                    onClick = { launcher.launch(getGoogleSignInIntent(context)) },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 20.dp)
//                        .height(45.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = ButtonPrimary),
//                    elevation = ButtonDefaults.buttonElevation(
//                        defaultElevation = 6.dp,
//                        pressedElevation = 8.dp
//                    ),
//                    enabled = !isLoading
//                ) {
//                    Text(
//                        text = stringResource(R.string.login_con_google),
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = ButtonPrimaryText
//                    )
//
//                }
//                Spacer(modifier = Modifier.height(50.dp))
//                Divider(
//                    color = Color.Gray,
//                    thickness = 1.dp,
//                    modifier = Modifier.padding(horizontal = 20.dp)
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                Text(
//                    text = stringResource(R.string.message_soporte),
//                    modifier = Modifier,
//                    style = TextStyle(
//                        color = TextPrimary,
//                        fontSize = 14.sp
//                    )
//                )

            }

        }
    }
    if (isLoading) {
        LoadingOverlay(modifier = Modifier.zIndex(1f))
    }
}

