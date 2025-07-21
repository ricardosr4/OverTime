package com.example.overtime.presentation.addHrsExtras.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.overtime.ui.theme.*

@Composable
fun SummaryCard(
    selectedDate: String,
    selectedPercentage: Int,
    selectedHours: Int,
    modifier: Modifier = Modifier
) {
    // Validar que todos los campos básicos sean válidos
    val validDate = selectedDate != "Selecciona una fecha"
    val validHours = selectedHours >= 1 && selectedHours <= 12
    val validPercentages = listOf(50, 75, 100, 130)
    val validPercentage = validPercentages.contains(selectedPercentage)
    
    if (validDate && validHours && validPercentage) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = SecondaryColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Resumen",
                        modifier = Modifier.size(24.dp),
                        tint = OnSecondary
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = "Resumen",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSecondary
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Fecha:",
                        fontSize = 14.sp,
                        color = OnSecondary.copy(alpha = 0.7f)
                    )
                    Text(
                        text = selectedDate,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = OnSecondary
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Horas:",
                        fontSize = 14.sp,
                        color = OnSecondary.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "$selectedHours horas",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = OnSecondary
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Porcentaje:",
                        fontSize = 14.sp,
                        color = OnSecondary.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "$selectedPercentage%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = OnSecondary
                    )
                }
            }
        }
    }
} 