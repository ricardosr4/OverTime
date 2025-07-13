package com.example.overtime.ui.screen.preLogin.view

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.overtime.R
import com.example.overtime.navigation.AppScreen
import com.example.overtime.ui.component.StandardButton
import com.example.overtime.ui.theme.ButtonDisabled
import com.example.overtime.ui.theme.ButtonPrimary
import com.example.overtime.ui.theme.ButtonPrimaryText
import com.example.overtime.ui.theme.DividerColor
import com.example.overtime.ui.theme.TextPrimary

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

            StandardButton(
                onClick = { navController.navigate(AppScreen.LoginScreen.route) },
                text = stringResource(R.string.login),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            StandardButton(
                onClick = { navController.navigate(AppScreen.RegisterScreen.route) },
                text = stringResource(R.string.register),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { }, //add fun for login to google and firebase
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonDisabled
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Text(
                    text = stringResource(R.string.login_con_google),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ButtonPrimaryText
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
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
                    text = stringResource(R.string.message_soporte),
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    style = TextStyle(
                        color = TextPrimary,
                        fontSize = 14.sp
                    )

                )

            }

        }

    }
}
