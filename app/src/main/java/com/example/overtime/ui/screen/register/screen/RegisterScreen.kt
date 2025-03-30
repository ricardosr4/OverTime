package com.example.overtime.ui.screen.register.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.overtime.R
import com.example.overtime.ui.component.ZetaButtonBasic
import com.example.overtime.ui.component.ZetaImageLogo
import com.example.overtime.ui.component.ZetaOutlinedTextField
import com.example.overtime.ui.component.ZetaSpaceHeight
import com.example.overtime.ui.component.ZetaText
import com.example.overtime.ui.component.ZetaTextLink
import com.example.overtime.ui.screen.register.viewModel.RegisterViewModel
import com.example.overtime.ui.theme.PrimaryColor

@Composable
fun RegisterScreen(navController: NavController) {

    val viewModel: RegisterViewModel = viewModel()
    val registerState by viewModel.registerState

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
                label = "Nombre"
            )
            ZetaSpaceHeight()
            ZetaOutlinedTextField(
                value = registerState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = "Email",
            )
            ZetaSpaceHeight()
            ZetaOutlinedTextField(
                value = registerState.password,
                onValueChange = { viewModel.onPasswordChanged(it)},
                label = "Paswword",
                isPassword = true //falta agregar funcion ´para ocultar password
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
            onClick = {if (registerState.isFormValid)
                viewModel.createUser { navController.navigate("login_screen") } },
            backgroundColor = PrimaryColor,
            text = "Registrase", color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(20.dp)



        )
    }
}


//val viewModel: RegisterViewModel = viewModel()
//    val registerState by viewModel.registerState
//    val context = LocalContext.current
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//            .verticalScroll(rememberScrollState())
//    ) {
//        Spacer(modifier = Modifier.height(70.dp))
//
//        Text(
//            text = stringResource(id = R.string.register),
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.Start)
//                .padding(start = 40.dp),
//            fontSize = 50.sp,
//            color = TextPrimary
//        )
//        Spacer(modifier = Modifier.height(70.dp))
//
//        OutlinedTextField(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 40.dp),
//            value = registerState.email,
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Email,
//                imeAction = ImeAction.Done
//            ),
//            onValueChange = {
//                viewModel.onEmailChanged(it)
//            },
//            leadingIcon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.icon_email),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .padding(start = 5.dp, end = 10.dp)
//                        .size(20.dp)
//                )
//            },
//
//            label = {
//                Text(
//                    text = stringResource(id = R.string.email),
//                    color = TextHint
//                )
//            }
//        )
//        Spacer(modifier = Modifier.height(40.dp))
//
//        OutlinedTextField(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 40.dp),
//            value = registerState.password,
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Password,
//                imeAction = ImeAction.Done
//            ),
//            onValueChange = { viewModel.onPasswordChanged(it) },
//            visualTransformation = registerState.passwordVisualTransformation,
//            leadingIcon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.icon_password),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .padding(start = 5.dp, end = 10.dp)
//                        .size(20.dp)
//                )
//            },
//            label = {
//                Text(
//                    text = stringResource(id = R.string.password),
//                    color = TextHint
//                )
//            }
//        )
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 28.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Checkbox(
//                checked = registerState.isPasswordVisible,
//                onCheckedChange = { viewModel.onPasswordVisibilityChanged() },
//                colors = CheckboxDefaults.colors(
//                    checkedColor = SecondaryColor
//                )
//            )
//            Text(
//                text = stringResource(id = R.string.mostrar_contraseña),
//                fontSize = 12.sp
//            )
//        }
//
//
//        OutlinedTextField(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 40.dp),
//            value = registerState.passwordConfirmation,
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Password,
//                imeAction = ImeAction.Done
//            ),
//            onValueChange = { viewModel.onPasswordConfirmationChanged(it) },
//            visualTransformation = registerState.passwordConfirmationVisualTransformation,
//            leadingIcon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.icon_password),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .padding(start = 5.dp, end = 10.dp)
//                        .size(20.dp)
//                )
//            },
//            label = {
//                Text(
//                    text = stringResource(R.string.confirm_password),
//                    color = TextHint
//                )
//            }
//        )
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 28.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Checkbox(
//                checked = registerState.isPasswordConfirmationVisible,
//                onCheckedChange = { viewModel.onPasswordConfirmationVisibilityChanged() },
//                colors = CheckboxDefaults.colors(
//                    checkedColor = SecondaryColor
//                )
//            )
//
//            Text(
//                text = stringResource(id = R.string.mostrar_contraseña),
//                fontSize = 12.sp
//            )
//        }
//        Spacer(modifier = Modifier.height(40.dp))
//
//        Text(
//            text = "Si ya tienes una cuenta, ingresa aqui!",
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .clickable { navController.navigate("login_screen") },
//            style = TextStyle(
//                color = PrimaryColor,
//                fontSize = 14.sp,
//                fontFamily = FontFamily.SansSerif
//            ),
//        )
//        Spacer(modifier = Modifier.height(100.dp))
//
//
//        Surface(
//            shape = RoundedCornerShape(8.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(45.dp)
//                .padding(horizontal = 40.dp)
//                .shadow(elevation = 10.dp, ambientColor = Color.Black)
//                .clickable {
//                    if (registerState.isFormValid) {
//                        viewModel.createUser {}
//                        navController.navigate("login_screen")
//                        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
//                    }
//                },
//            color = if (registerState.isFormValid) SecondaryColor else Color.Gray
//
//        ) {
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier
//                    .fillMaxSize()
//
//                //add button color disable
//            ) {
//                Text(
//                    text = stringResource(id = R.string.register),
//                    color = ButtonPrimaryText,
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    textAlign = TextAlign.Center,
////                    fontFamily = FontFamily(getFont(Fonts.ROBOTO_BOLD))
//                )
//            }
//        }
//
//    }