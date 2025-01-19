package com.example.overtime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.overtime.navigation.NavGraph
import com.example.overtime.ui.bottomNavigationBar.BottomNavigationBar
import com.example.overtime.ui.theme.OverTimeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OverTimeTheme {
                OverTimeApp()
            }
        }
    }
}

@Composable
fun OverTimeApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            // Mostrar BottomNavigationBar solo en pantallas específicas
            if (shouldShowBottomBar(navController)) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavGraph(navController = navController)
        }
    }
}


@Composable
fun shouldShowBottomBar(navController: NavController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val noBottomBarRoutes = listOf(
        "splash_screen",
        "pre_login_screen",
        "login_screen",
        "register_screen")

    return currentDestination !in noBottomBarRoutes
}
