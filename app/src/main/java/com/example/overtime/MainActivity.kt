package com.example.overtime

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.overtime.core.prefs.PreferencesManager
import com.example.overtime.core.prefs.ThemeMode
import com.example.overtime.presentation.components.BottomNavigationBar
import com.example.overtime.presentation.navigation.NavGraph
import com.example.overtime.ui.theme.OverTimeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppRoot(preferencesManager)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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
        "register_screen",
        "onboarding_screen")

    return currentDestination !in noBottomBarRoutes
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppRoot(preferencesManager: PreferencesManager) {
    val themeMode by preferencesManager.themeModeFlow.collectAsState(initial = ThemeMode.SYSTEM)
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    OverTimeTheme(darkTheme = darkTheme) {
        OverTimeApp()
    }
}
