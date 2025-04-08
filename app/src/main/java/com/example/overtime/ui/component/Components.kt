package com.example.overtime.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.overtime.ui.theme.PrimaryColor
import com.example.overtime.ui.theme.TextHint



@Composable
fun ZetaSpaceHeight(size: Dp = 10.dp) {
    Spacer(modifier = Modifier.height(size))
}

@Composable
fun ZetaTextLink(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = Color.Black,
    textLink: String,
    linkColor: Color = Color.Blue,
    onClick: () -> Unit,
    // Modifier externo
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = textColor,

        )
        Spacer(modifier = Modifier.size(4.dp)) // espacio entre textos
        Text(
            text = textLink,
            color = linkColor,

            modifier = modifier.clickable(onClick = onClick)
        )
    }
}

@Composable
fun ZetaImageLogo(
    image: Painter,           // Imagen a mostrar
    modifier: Modifier = Modifier, // Modificadores para tamaño y posición
    contentDescription: String = "", // Descripción de la imagen
    width: Dp = 100.dp,       // Ancho de la imagen
    height: Dp = 100.dp,      // Alto de la imagen
    contentScale: ContentScale = ContentScale.Crop // Escala de la imagen
) {
    Box(modifier = modifier) {
        Image(
            painter = image,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(width, height),
            contentScale = contentScale
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZetaOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onVisibilityToggle: (() -> Unit)? = null,
    leadingIcon: Painter? = null,
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = TextHint) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    painter = it,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(20.dp)
                )
            }
        },
        trailingIcon = {
            if (isPassword && onVisibilityToggle != null) {
                IconButton(onClick = onVisibilityToggle) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = PrimaryColor,
            unfocusedBorderColor = Color.Gray,
        )
    )
}

@Composable
fun CustomPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    showPassword: Boolean,
    onShowPasswordChange: (Boolean) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = showPassword,
            onCheckedChange = onShowPasswordChange
        )
        Text(text = "Show Password")
    }
}

@Composable
fun ZetaButtonBasic(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    text: String,
    color: Color = MaterialTheme.colorScheme.primary,
    textSize: TextUnit = 16.sp,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = color,
            containerColor = backgroundColor
        ),
        modifier = modifier




    ) {
        Text(text = text, fontSize = textSize)
    }

}

@Composable
fun ZetaText(
    text: String,                             // Texto a mostrar
    modifier: Modifier = Modifier,            // Modificador para padding, tamaño, etc.
    fontSize: TextUnit = 16.sp,               // Tamaño del texto (por defecto 16sp)
    color: Color = Color.Black,               // Color del texto
    maxLines: Int = Int.MAX_VALUE,            // Máximo de líneas visibles

) {
    Text(
        text = text,
        fontSize = fontSize,
        color = color,
        modifier = modifier,
        maxLines = maxLines,
    )
}

@Composable
fun ZetaAlertDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    val scroll = rememberScrollState(0)

    AlertDialog(
        onDismissRequest = { onDismissClick() },
        title = { Text(text = title) },
        text = {
            Text(
                text = message,
                textAlign = TextAlign.Justify,
                modifier = Modifier.verticalScroll(scroll)
            )
        },
        confirmButton = {
            Button(onClick = { onConfirmClick() }) {
                Text(text = confirmText)
            }
        }
    )
}