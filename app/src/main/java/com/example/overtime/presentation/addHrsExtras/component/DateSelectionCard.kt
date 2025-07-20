package com.example.overtime.presentation.addHrsExtras.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.overtime.ui.theme.*

@Composable
fun DateSelectionCard(
    selectedDate: String,
    onDateButtonClick: () -> Unit
) {
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
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendario",
                    modifier = Modifier.size(24.dp),
                    tint = PrimaryColor
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = "Fecha de Trabajo",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurface
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedButton(
                onClick = onDateButtonClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = CardColor.copy(alpha = 0.3f)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            PrimaryColor,
                            BlueDark
                        )
                    )
                )
            ) {
                Text(
                    text = selectedDate,
                    fontSize = 16.sp,
                    color = PrimaryColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
} 