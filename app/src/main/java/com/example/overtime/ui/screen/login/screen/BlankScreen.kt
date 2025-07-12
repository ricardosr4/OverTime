package com.example.overtime.ui.screen.login.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.overtime.navigation.AppScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun BlankScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        if (!Firebase.auth.currentUser?.email.isNullOrEmpty()) {
            // Si está logueado, va directo al HomeScreen
            navController.navigate(AppScreen.HomeScreen.route) {
                popUpTo(AppScreen.BlankScreen.route) { inclusive = true }
            }
        } else {
            // Si NO está logueado, va al SplashScreen
            navController.navigate(AppScreen.SplashScreen.route) {
                popUpTo(AppScreen.BlankScreen.route) { inclusive = true }
            }
        }
    }
}
