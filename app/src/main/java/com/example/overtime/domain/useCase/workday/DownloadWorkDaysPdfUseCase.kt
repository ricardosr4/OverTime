package com.example.overtime.domain.useCase.workday

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.overtime.data.model.WorkDay
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DownloadWorkDaysPdfUseCase @Inject constructor() {
    operator fun invoke(
        context: Context,
        logo: Bitmap?,
        userName: String,
        workDays: List<WorkDay>
    ): Result<File> {
        return try {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
            val page = pdfDocument.startPage(pageInfo)
            val canvas: Canvas = page.canvas
            val paint = Paint()
            var y = 40

            // Logo
            logo?.let {
                val scaledLogo = Bitmap.createScaledBitmap(it, 120, 120, true)
                canvas.drawBitmap(scaledLogo, (canvas.width - scaledLogo.width) / 2f, y.toFloat(), paint)
                y += scaledLogo.height + 30 // Más espacio después del logo
            }

            // Nombre de usuario
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 18f
            paint.isFakeBoldText = true
            canvas.drawText(userName, (canvas.width / 2).toFloat(), y.toFloat(), paint)
            y += 30

            // Título
            paint.textSize = 22f
            canvas.drawText("Reporte de Horas Extras", (canvas.width / 2).toFloat(), y.toFloat(), paint)
            y += 30

            // Fecha de generación
            paint.textSize = 14f
            paint.isFakeBoldText = false
            val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            canvas.drawText("Generado: $dateStr", (canvas.width / 2).toFloat(), y.toFloat(), paint)
            y += 30

            // Encabezado de tabla
            paint.textSize = 16f
            paint.isFakeBoldText = true
            paint.textAlign = Paint.Align.CENTER
            val startX = 40
            val colWidths = listOf(180, 120, 180)
            val headers = listOf("Fecha", "Horas", "Porcentaje de Hrs")
            var x = startX
            for ((i, header) in headers.withIndex()) {
                canvas.drawText(header, x + colWidths[i] / 2f, y.toFloat(), paint)
                x += colWidths[i]
            }
            y += 24

            // Filas de la tabla
            paint.textSize = 14f
            paint.isFakeBoldText = false
            for (workDay in workDays) {
                x = startX
                val row = listOf(
                    workDay.weekDay, // Ahora 'Fecha' muestra el valor de weekDay
                    workDay.quantityOverHours.toString(),
                    workDay.percentageOverHours.toString()
                )
                for ((i, cell) in row.withIndex()) {
                    canvas.drawText(cell, x + colWidths[i] / 2f, y.toFloat(), paint)
                    x += colWidths[i]
                }
                y += 20
                if (y > 800) break // Evitar desbordar la página
            }

            // Pie de página
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 12f
            y = 820
            canvas.drawText("OverTime App", (canvas.width / 2).toFloat(), y.toFloat(), paint)

            pdfDocument.finishPage(page)

            // Guardar en carpeta de descargas
            val fileName = "HorasExtras_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.pdf"
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)
            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()
            Result.success(file)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 