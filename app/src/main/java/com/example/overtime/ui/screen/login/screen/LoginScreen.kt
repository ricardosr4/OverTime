package com.example.overtime.ui.screen.login.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.overtime.R
import com.example.overtime.navigation.AppScreen
import com.example.overtime.ui.component.ZetaButtonBasic
import com.example.overtime.ui.component.ZetaImageLogo
import com.example.overtime.ui.component.ZetaOutlinedTextField
import com.example.overtime.ui.component.ZetaSpaceHeight
import com.example.overtime.ui.component.ZetaText
import com.example.overtime.ui.component.ZetaTextLink
import com.example.overtime.ui.theme.ButtonPrimaryText
import com.example.overtime.ui.theme.TextPrimary
import com.example.overtime.ui.theme.PrimaryColor
import com.example.overtime.ui.theme.SecondaryColor
import com.example.overtime.ui.theme.TextHint
import com.example.overtime.ui.screen.login.viewModel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController) {

    val viewModel: LoginViewModel = viewModel()
    val loginState by viewModel.loginState

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
                leadingIcon = painterResource(id = R.drawable.icon_email)
            )
            ZetaSpaceHeight()
            ZetaOutlinedTextField(
                value = loginState.password,
                onValueChange = { viewModel.onPasswordChanged(it)},
                label = "Paswword",
                leadingIcon = painterResource(id = R.drawable.icon_password),
                isPassword = true

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
                text = "Recupera contraseña", linkColor = PrimaryColor,
                textLink = "aqui!!",
                onClick = { viewModel.resetPassword( loginState.email){} },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )


        }
        ZetaButtonBasic(
            onClick = {if (loginState.isFormValid)
                viewModel.login(email = loginState.email, password = loginState.password)
                { navController.navigate("home_screen") } },
            backgroundColor = PrimaryColor,
            text = "Login", color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(20.dp)



        )
    }

}



//val context = LocalContext.current
//
//
//
//Column(
//modifier = Modifier
//.fillMaxSize()
//.background(Color.White)
//.verticalScroll(rememberScrollState())
//) {
//    Spacer(modifier = Modifier.height(70.dp))
//
//    Text(
//        text = stringResource(id = R.string.login),
//        modifier = Modifier
//            .fillMaxWidth()
//            .align(Alignment.Start)
//            .padding(start = 40.dp),
//        fontSize = 50.sp,
//        color = TextPrimary
//    )
//    Spacer(modifier = Modifier.height(70.dp))
//
//    OutlinedTextField(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 40.dp),
//        value = loginState.email,
//        singleLine = true,
//        keyboardOptions = KeyboardOptions(
//            keyboardType = KeyboardType.Email,
//            imeAction = ImeAction.Done
//        ),
//        onValueChange = { viewModel.onEmailChanged(it) },
//        leadingIcon = {
//            Icon(
//                painter = painterResource(id = R.drawable.icon_email),
//                contentDescription = "",
//                modifier = Modifier
//                    .padding(start = 5.dp, end = 10.dp)
//                    .size(20.dp)
//            )
//        },
//        label = {
//            Text(
//                text = stringResource(R.string.email),
//                color = TextHint
//            )
//        }
//    )
//    Spacer(modifier = Modifier.height(30.dp))
//
//    OutlinedTextField(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 40.dp),
//        value = loginState.password,
//        singleLine = true,
//        keyboardOptions = KeyboardOptions(
//            keyboardType = KeyboardType.Password,
//            imeAction = ImeAction.Done
//        ),
//        onValueChange = { viewModel.onPasswordChanged(it) },
//        visualTransformation = loginState.passwordVisualTransformation,
//        leadingIcon = {
//            Icon(
//                painter = painterResource(id = R.drawable.icon_password),
//                contentDescription = "",
//                modifier = Modifier
//                    .padding(start = 5.dp, end = 10.dp)
//                    .size(20.dp)
//            )
//        },
//        label = {
//            Text(
//                text = stringResource(R.string.password),
//                color = TextHint
//            )
//        }
//    )
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 28.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Checkbox(
//            checked = loginState.isPasswordVisible,
//            onCheckedChange = { viewModel.onPasswordVisibilityChanged() },
//            colors = CheckboxDefaults.colors(
//                checkedColor = SecondaryColor,
//            )
//        )
//        Text(
//            text = stringResource(R.string.mostrar_contraseña),
//            fontSize = 12.sp
//        )
//    }
//    Spacer(modifier = Modifier.height(50.dp))
//
//    Text(
//        text = "Si no tienes una cuenta, registrate aqui !",
//        modifier = Modifier
//            .align(Alignment.CenterHorizontally)
//            .clickable {  navController.navigate("register_screen") },
//        style = TextStyle(
//            color = PrimaryColor,
//            fontSize = 14.sp,
//            fontFamily = FontFamily.SansSerif
//        ),
//    )
//    Spacer(modifier = Modifier.height(30.dp))
//
//    Text(
//        text = stringResource(R.string.recuperar_contraseña),
//        modifier = Modifier
//            .align(Alignment.CenterHorizontally)
//            .clickable { viewModel.resetPassword(loginState.email){} },
//        style = TextStyle(
//            color = PrimaryColor,
//            fontSize = 14.sp,
//            fontFamily = FontFamily.SansSerif
//        ),
//    )
//    Spacer(modifier = Modifier.height(170.dp))
//
//
//    Surface(
//        shape = RoundedCornerShape(8.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(45.dp)
//            .padding(horizontal = 40.dp)
//            .shadow(elevation = 10.dp, ambientColor = Color.Black)
//            .clickable { if (loginState.isFormValid) viewModel.login(loginState.email,loginState.password){
//                navController.navigate(AppScreen.HomeScreen.route)
//                Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show()
//            }  },
//        color = if (loginState.isFormValid) SecondaryColor else Color.Gray
//    ) {
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Text(
//                text = stringResource(R.string.login),
//                color = ButtonPrimaryText,
//                modifier = Modifier.fillMaxWidth(),
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}
