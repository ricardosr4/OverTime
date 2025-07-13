package com.example.overtime.ui.screen.configuration.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.overtime.ui.theme.CardColor

@Composable
fun AppInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(CardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Información",
                modifier = Modifier.size(48.dp),
                tint = Color.Black
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Información de la App",
                fontSize = 18.sp,
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nombre:",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(0.3f)
                )
                Text(
                    text = "OverTime",
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(0.7f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Versión:",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(0.3f)
                )
                Text(
                    text = "Prueba",
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(0.7f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Desarrollador:",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(0.3f)
                )
                Text(
                    text = "OverTime Team",
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(0.7f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Descripción:",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(0.3f)
                )
                Text(
                    text = "Gestión de horas extras",
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(0.7f)
                )
            }
        }
    }
} 