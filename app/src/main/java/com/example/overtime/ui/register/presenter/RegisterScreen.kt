package com.example.overtime.ui.register.presenter

import android.widget.Toast
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
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.overtime.R
import com.example.overtime.ui.register.viewModel.RegisterViewModel
import com.example.overtime.ui.theme.ButtonPrimary
import com.example.overtime.ui.theme.ButtonPrimaryText
import com.example.overtime.ui.theme.DividerColor
import com.example.overtime.ui.theme.PrimaryColor
import com.example.overtime.ui.theme.SecondaryColor
import com.example.overtime.ui.theme.TextHint
import com.example.overtime.ui.theme.TextPrimary


@Composable
fun RegisterScreen(navController: NavController) {

    val viewModel: RegisterViewModel = viewModel()
    val registerState by viewModel.registerState
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(70.dp))

        Text(
            text = stringResource(id = R.string.register),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
                .padding(start = 40.dp),
            fontSize = 50.sp,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(70.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            value = registerState.email,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            onValueChange = {
                viewModel.onEmailChanged(it)
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
                Text(
                    text = stringResource(id = R.string.email),
                    color = TextHint
                )
            }
        )
        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            value = registerState.password,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            onValueChange = { viewModel.onPasswordChanged(it) },
            visualTransformation = registerState.passwordVisualTransformation,
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
                Text(
                    text = stringResource(id = R.string.password),
                    color = TextHint
                )
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = registerState.isPasswordVisible,
                onCheckedChange = { viewModel.onPasswordVisibilityChanged() },
                colors = CheckboxDefaults.colors(
                    checkedColor = SecondaryColor
                )
            )
            Text(
                text = stringResource(id = R.string.mostrar_contraseña),
                fontSize = 12.sp
            )
        }


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            value = registerState.passwordConfirmation,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            onValueChange = { viewModel.onPasswordConfirmationChanged(it) },
            visualTransformation = registerState.passwordConfirmationVisualTransformation,
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
                Text(
                    text = stringResource(R.string.confirm_password),
                    color = TextHint
                )
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = registerState.isPasswordConfirmationVisible,
                onCheckedChange = { viewModel.onPasswordConfirmationVisibilityChanged() },
                colors = CheckboxDefaults.colors(
                    checkedColor = SecondaryColor
                )
            )

            Text(
                text = stringResource(id = R.string.mostrar_contraseña),
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Si ya tienes una cuenta, ingresa aqui!",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { navController.navigate("login_screen") },
            style = TextStyle(
                color = PrimaryColor,
                fontSize = 14.sp,
                fontFamily = FontFamily.SansSerif
            ),
        )
        Spacer(modifier = Modifier.height(100.dp))


        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .padding(horizontal = 40.dp)
                .shadow(elevation = 10.dp, ambientColor = Color.Black)
                .clickable {
                    if (registerState.isFormValid) {
                        viewModel.register()
                        navController.navigate("login_screen")
                        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    }
                },
            color = if (registerState.isFormValid) ButtonPrimary else Color.Gray

        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()

                //add button color disable
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    color = ButtonPrimaryText,
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
            color = DividerColor,
            thickness = 1.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(id = R.string.message_soporte),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
            style = TextStyle(
                color = (TextPrimary),
                fontSize = 14.sp
            )
        )
    }
}