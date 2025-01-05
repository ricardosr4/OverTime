package com.example.overtime.ui.preLogin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.overtime.R
import com.example.overtime.navigation.AppScreen

@Composable
fun PreLoginScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(horizontal = 16.dp, vertical = 16.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_pre_login),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 10.dp,
                            bottomEnd = 10.dp
                        )
                    )
            )
            Spacer(modifier = Modifier.height(50.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(horizontal = 20.dp)
                    .shadow(elevation = 10.dp, ambientColor = Color.Black)
                    .clickable { navController.navigate(AppScreen.LoginScreen.route) }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Blue)
                ) {
                    Text(
                        text = "Login",
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp

                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(horizontal = 20.dp)
                    .shadow(elevation = 10.dp, ambientColor = Color.Black)
                    .clickable { navController.navigate(AppScreen.RegisterScreen.route) }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Blue)
                ) {
                    Text(
                        text = "Register",
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                        //fontFamily = FontFamily(getFont(Fonts.ROBOTO_BOLD)) -> definir fuente

                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(horizontal = 20.dp)
                    .shadow(elevation = 10.dp, ambientColor = Color.Black)
                    .clickable { } //agregar funcion para entrar con google
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Blue)
                ) {
                    Text(
                        text = "Registrarse con google",
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                        //fontFamily = FontFamily(getFont(Fonts.ROBOTO_BOLD)) -> definir fuente

                    )
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

    }
}
