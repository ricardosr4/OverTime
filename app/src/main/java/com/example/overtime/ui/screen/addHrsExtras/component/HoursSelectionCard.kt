package com.example.overtime.ui.screen.addHrsExtras.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
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
fun HoursSelectionCard(
    selectedHours: Int,
    onHoursSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val hoursOptions = (1..12).toList()

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
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Horas",
                    modifier = Modifier.size(24.dp),
                    tint = PrimaryColor
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = "Horas Extras Trabajadas",
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
                    value = if (selectedHours == 0) "Selecciona las horas" else "$selectedHours horas",
                    onValueChange = {},
                    label = { Text("Selecciona las horas") },
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
                    hoursOptions.forEach { hour ->
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    "$hour horas",
                                    fontWeight = if (hour == selectedHours) FontWeight.Bold else FontWeight.Normal
                                ) 
                            },
                            onClick = {
                                onHoursSelected(hour)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
} 