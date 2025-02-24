package com.example.overtime.ui.addHrsExtras

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.overtime.ui.theme.ButtonPrimary
import com.example.overtime.ui.theme.CardColor
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHrsExtrasScreen() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("Selecciona una fecha") }
    var selectedPercentage by remember { mutableIntStateOf(50) }
    var selectedHours by remember { mutableIntStateOf(1) }

    val percentageOptions = listOf(50, 75, 100, 130)
    val hoursOptions = (1..12).toList()

    var expandedPercentage by remember { mutableStateOf(false) }
    var expandedHours by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card para seleccionar la fecha
            Card(
                colors = CardDefaults.cardColors(containerColor = CardColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),

            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Seleccionar Fecha",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { showDatePicker = true },
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = selectedDate, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
            Divider()
            Spacer(modifier = Modifier.height(50.dp))

            // Card para seleccionar el porcentaje de horas extras
            Card(
                colors = CardDefaults.cardColors(containerColor = CardColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Selecciona el porcentaje de horas extras",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = expandedPercentage,
                        onExpandedChange = { expandedPercentage = !expandedPercentage }
                    ) {
                        TextField(
                            readOnly = true,
                            value = "$selectedPercentage%",
                            onValueChange = {},
                            label = { Text("Porcentaje") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expandedPercentage,
                            onDismissRequest = { expandedPercentage = false }
                        ) {
                            percentageOptions.forEach { percentage ->
                                DropdownMenuItem(
                                    text = { Text("$percentage%") },
                                    onClick = {
                                        selectedPercentage = percentage
                                        expandedPercentage = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
            Divider()
            Spacer(modifier = Modifier.height(50.dp))

            // Card para seleccionar la cantidad de horas extras
            Card(
                colors = CardDefaults.cardColors(containerColor = CardColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Selecciona las horas extras",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = expandedHours,
                        onExpandedChange = { expandedHours = !expandedHours }
                    ) {
                        TextField(
                            readOnly = true,
                            value = "$selectedHours hrs",
                            onValueChange = {},
                            label = { Text("Horas Extras") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expandedHours,
                            onDismissRequest = { expandedHours = false }
                        ) {
                            hoursOptions.forEach { hour ->
                                DropdownMenuItem(
                                    text = { Text("$hour hrs") },
                                    onClick = {
                                        selectedHours = hour
                                        expandedHours = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Botón fijo en la parte inferior
        Button(
            onClick = { /* Acción para agregar */ },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(20.dp)
        ) {
            Text(text = "Agregar", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }

    // Mostrar el DatePicker cuando se activa
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Aceptar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        ) {
            val datePickerState = rememberDatePickerState()
            DatePicker(state = datePickerState)

            LaunchedEffect(datePickerState.selectedDateMillis) {
                datePickerState.selectedDateMillis?.let { millis ->
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    selectedDate = sdf.format(Date(millis))
                }
            }
        }
    }
}
