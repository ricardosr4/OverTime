package com.example.overtime.ui.login.presenter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.overtime.R
import com.example.overtime.navigation.AppScreen


@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    var checkPassword by remember { mutableStateOf(false) }
    var visualTransformationPassword by remember { mutableStateOf<VisualTransformation> (PasswordVisualTransformation()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(70.dp))

        Text(
            text = "Login",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
                .padding(start = 40.dp),
            fontSize = 50.sp
        )
        Spacer(modifier = Modifier.height(70.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            value = email,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            onValueChange = {
                email = it

            },
//            placeholder = { Text(text = "stringResource(EMAIL)", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_email),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(start = 5.dp, end = 10.dp)
                        .size(20.dp)
                )
            },

            label = {
                Text(text = "Email", color = Color.Gray)
            }
        )
        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            value = password,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            onValueChange = {
                password = it

            },
            visualTransformation = visualTransformationPassword,
//            placeholder = { Text(text = "stringResource(EMAIL)", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_password),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(start = 5.dp, end = 10.dp)
                        .size(20.dp)
                )
            },
            label = {
                Text(text = "Password", color = Color.Gray)
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checkPassword,
                onCheckedChange = {
                checkPassword = it

            })
            Text(text = "Mostrar contraseña")
        }
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Recuperar tu Contraseña!",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {  },
            style = TextStyle(
                color = Color.Blue,
                fontSize = 14.sp,
            ),
        )
        Spacer(modifier = Modifier.height(170.dp))

        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .padding(horizontal = 40.dp)
                .shadow(elevation = 10.dp, ambientColor = Color.Black)
                .clickable { navController.navigate(AppScreen.HomeScreen.route) } //agregar funcion para registrar con firebase
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue)
                //aqui podemos agregar un color de boton antes de activacion
            ) {
                Text(
                    text = "Login",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
//                    fontFamily = FontFamily(getFont(Fonts.ROBOTO_BOLD))
                )
            }
        }


    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Divider(
            color = Color.Black,
            thickness = 1.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "En caso de problemas comunicate con Soporte",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
            style = TextStyle(
                color = (Color.Black),
                fontSize = 14.sp
            )
        )
    }
}