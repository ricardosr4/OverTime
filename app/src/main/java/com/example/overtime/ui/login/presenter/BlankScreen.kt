package com.example.overtime.ui.login.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun BlankScreen(navController: NavController) {

    LaunchedEffect(Unit) {
        if (!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            navController.navigate("home_screen")

        } else {
            navController.navigate("splash_screen")
        }
    }
}
