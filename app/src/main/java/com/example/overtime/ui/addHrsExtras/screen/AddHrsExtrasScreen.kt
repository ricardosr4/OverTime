package com.example.overtime.ui.addHrsExtras.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.overtime.data.model.WorkDay
import com.example.overtime.ui.addHrsExtras.state.AddHrsExtrasState
import com.example.overtime.ui.addHrsExtras.viewModel.AddHrsExtrasViewModel
import com.example.overtime.ui.theme.ButtonPrimary
import com.example.overtime.ui.theme.CardColor
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHrsExtrasScreen(navController: NavController, viewModel: AddHrsExtrasViewModel = viewModel()) {
    val state = viewModel.state.value

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
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Seleccionar Fecha", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.onShowDatePicker(true) },
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = state.selectedDate, fontSize = 16.sp)
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
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Selecciona el porcentaje de horas extras", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandedPercentage,
                        onExpandedChange = { expandedPercentage = !expandedPercentage }
                    ) {
                        TextField(
                            readOnly = true,
                            value = "${state.selectedPercentage}%",
                            onValueChange = {},
                            label = { Text("Porcentaje") },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                        )
                        ExposedDropdownMenu(
                            expanded = expandedPercentage,
                            onDismissRequest = { expandedPercentage = false }
                        ) {
                            percentageOptions.forEach { percentage ->
                                DropdownMenuItem(
                                    text = { Text("$percentage%") },
                                    onClick = {
                                        viewModel.onPercentageSelected(percentage)
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

            // Card para seleccionar las horas extras
            Card(
                colors = CardDefaults.cardColors(containerColor = CardColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Selecciona las horas extras", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandedHours,
                        onExpandedChange = { expandedHours = !expandedHours }
                    ) {
                        TextField(
                            readOnly = true,
                            value = "${state.selectedHours} hrs",
                            onValueChange = {},
                            label = { Text("Horas Extras") },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                        )
                        ExposedDropdownMenu(
                            expanded = expandedHours,
                            onDismissRequest = { expandedHours = false }
                        ) {
                            hoursOptions.forEach { hour ->
                                DropdownMenuItem(
                                    text = { Text("$hour hrs") },
                                    onClick = {
                                        viewModel.onHoursSelected(hour)
                                        expandedHours = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Botón para agregar el día de trabajo
        Button(
            onClick = {
                if (state.selectedDate == "Selecciona una fecha" || state.selectedHours == 0) {
                    viewModel.onShowErrorDialog(true)
                } else {
                    // Guardar los datos en Firebase
                    val newWorkDay = WorkDay(
                        weekDay = state.selectedDate,
                        quantityOverHours = state.selectedHours,
                        percentageOverHours = state.selectedPercentage
                    )
                    viewModel.addWorkDay(newWorkDay)
                    navController.popBackStack() // Regresar a Home
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(20.dp)
        ) {
            Text(text = "Agregar", fontSize = 20.sp)
        }
    }

    // Mostrar el DatePicker cuando se activa
    if (state.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { viewModel.onShowDatePicker(false) },
            confirmButton = {
                TextButton(onClick = { viewModel.onShowDatePicker(false) }) {
                    Text("Aceptar")
                }
            }
        ) {
            val datePickerState = rememberDatePickerState()
            DatePicker(state = datePickerState)

            LaunchedEffect(datePickerState.selectedDateMillis) {
                datePickerState.selectedDateMillis?.let { millis ->
                    val sdf = SimpleDateFormat("EEEE dd/MM/yyyy", Locale.getDefault())
                    viewModel.onDateSelected(sdf.format(Date(millis)).replaceFirstChar { it.uppercase() })
                }
            }
        }
    }

    // Mostrar AlertDialog si falta algún campo
    if (state.showErrorDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onShowErrorDialog(false) },
            title = { Text("Error") },
            text = { Text("Falta llenar un campo.") },
            confirmButton = {
                TextButton(onClick = { viewModel.onShowErrorDialog(false) }) {
                    Text("Aceptar", color = Color.Red)
                }
            }
        )
    }
}
