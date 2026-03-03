package com.example.overtime.presentation.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.overtime.R
import com.example.overtime.presentation.login.components.ZetaAlertDialog

@Composable
fun HomeActionsRow(
    hasWorkDays: Boolean,
    onDeleteAll: () -> Unit,
    onDownloadPdf: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            enabled = hasWorkDays
        ) {
            Text(
                text = stringResource(R.string.home_delete_hours),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onDownloadPdf,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            enabled = hasWorkDays
        ) {
            Text(
                text = stringResource(R.string.home_download_pdf),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
    if (showDialog) {
        ZetaAlertDialog(
            title = stringResource(R.string.common_alert_title),
            message = stringResource(R.string.home_delete_all_confirmation),
            confirmText = stringResource(R.string.common_ok),
            onConfirmClick = {
                onDeleteAll()
                showDialog = false
            },
            onDismissClick = { showDialog = false }
        )
    }
}