package com.example.overtime.ui.screen.addHrsExtras.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.overtime.ui.theme.*

@Composable
fun AddHrsExtrasButton(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onAddClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar",
                modifier = Modifier.size(24.dp),
                tint = ButtonPrimaryText
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = "Agregar Horas Extras",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ButtonPrimaryText
            )
        }
    }
} 