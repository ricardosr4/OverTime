package com.example.overtime.ui.configuration

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ConfigScreen(){
    Text(text = "Configuración")
}

//Formula para obtener horas extras
//        sueldo base $
//        dividido x 30
//        multiplicado x 7
//        dividido 44
//        multiplicado x 1.5 = hrs al 50%
//        multiplicado x 1.75 = hrs al 75%
//        multiplicado x 2.0 = hrs al 100%
//        multiplicado x 2.3 = hrs al 130%