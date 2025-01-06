package com.example.overtime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.overtime.navigation.NavGraph
import com.example.overtime.ui.theme.OverTimeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navigationController = rememberNavController()
            OverTimeTheme {
                NavGraph(navController = navigationController)

            }
        }
    }
}
//conflicto solucionado
// problema solucionado gracias a chat gpt
//los problemas siguen
// una prueba mas
// prueba 3
// prueba 4


