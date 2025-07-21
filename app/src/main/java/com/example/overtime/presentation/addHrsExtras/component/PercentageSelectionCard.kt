package com.example.overtime.presentation.addHrsExtras.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.overtime.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PercentageSelectionCard(
    selectedPercentage: Int,
    onPercentageSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val percentageOptions = listOf(50, 75, 100, 130)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Surface
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
                    imageVector = Icons.Default.Percent,
                    contentDescription = "Porcentaje",
                    modifier = Modifier.size(24.dp),
                    tint = PrimaryColor
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = "Porcentaje de Horas Extras",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurface
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = if (selectedPercentage == 0) "Selecciona el porcentaje" else "$selectedPercentage%",
                    onValueChange = {},
                    label = { Text("Selecciona el porcentaje") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = DividerColor,
                        focusedLabelColor = PrimaryColor
                    ),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    percentageOptions.forEach { percentage ->
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    "$percentage%",
                                    fontWeight = if (percentage == selectedPercentage) FontWeight.Bold else FontWeight.Normal
                                ) 
                            },
                            onClick = {
                                onPercentageSelected(percentage)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
} 