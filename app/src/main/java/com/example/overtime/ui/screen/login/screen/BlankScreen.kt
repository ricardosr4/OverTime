package com.example.overtime.ui.screen.login.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun BlankScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        if (!Firebase.auth.currentUser?.email.isNullOrEmpty()) {
            navController.navigate("home_screen") {
                popUpTo("blank_screen") { inclusive = true }
            }
        } else {
            navController.navigate("pre_login_screen") {
                popUpTo("blank_screen") { inclusive = true }
            }
        }
    }
}
