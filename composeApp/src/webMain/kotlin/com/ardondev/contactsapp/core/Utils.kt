package com.ardondev.contactsapp.core

import androidx.compose.foundation.ScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.browser.document
import org.jetbrains.skia.Image
import org.w3c.dom.HTMLInputElement
import org.w3c.files.File
import org.w3c.files.get
import kotlin.io.encoding.Base64

/**
 * Abre el explorador de archivos y llama a la función de callback con el archivo seleccionado.
 * @param onFileSelected Función a llamar con el archivo (File) seleccionado.
 * @param accept String de tipos de archivos aceptados.
 */
fun openFileExplorer(accept: String, onFileSelected: (File?) -> Unit) {
    // Crear un elemento de input de tipo 'file' en memoria
    val input = document.createElement("input") as HTMLInputElement
    input.type = "file"
    input.accept = accept // Define los tipos de archivos aceptados.

    // Establecer el manejador de eventos 'change'
    input.onchange = { event ->
        val fileList = input.files
        // Obtener el primer archivo seleccionado.
        val file = fileList?.get(0)
        onFileSelected(file)
        // El manejador de eventos debe devolver Unit o Nothing?
        null
    }

    // Simular un clic en el input para abrir el explorador
    input.click()
}

fun String.decodeBase64ToByteArray(): ByteArray {
    val byteArray = encodeToByteArray()
    return Base64.decode(byteArray, 0, byteArray.size)
}

fun ByteArray.toImageBitmap() = Image.makeFromEncoded(this).toComposeImageBitmap()

/**
 * Dibuja una barra de desplazamiento vertical customizada en el Composable al que se aplica.
 *
 * @param state El [ScrollState] asociado al contenido scrollable.
 * @param width El ancho de la barra de desplazamiento. Por defecto es 4.dp.
 * @param color El color utilizado para dibujar la barra.
 * @return [Modifier] que añade el dibujo de la barra de desplazamiento.
 */
fun Modifier.simpleVerticalScrollbar(
    state: ScrollState,
    width: Dp = 8.dp,
    color: Color = Color.DarkGray
): Modifier = drawBehind {
    val contentHeight = state.maxValue + size.height
    if (contentHeight <= 0f) return@drawBehind

    val trackWidth = width.toPx()
    val trackLeft = size.width - trackWidth
    val trackSize = Size(trackWidth, size.height)

    // Dibujar fondo (pista) con esquinas redondeadas
    drawRoundRect(
        color = color.copy(alpha = 0.12f),
        topLeft = Offset(trackLeft, 0f),
        size = trackSize,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(trackWidth / 2f, trackWidth / 2f),
        alpha = 1f
    )

    // Calcular tamaño y offset del pulgar (thumb)
    val rawThumbHeight = size.height * (size.height / contentHeight)
    val minThumbHeight = 24.dp.toPx()
    val thumbHeight = kotlin.math.max(rawThumbHeight, minThumbHeight)

    val rawThumbOffset = state.value * (size.height / contentHeight)
    val thumbOffset = rawThumbOffset.coerceIn(0f, size.height - thumbHeight)

    // Dibujar pulgar con esquinas redondeadas
    drawRoundRect(
        color = color,
        topLeft = Offset(trackLeft, thumbOffset),
        size = Size(trackWidth, thumbHeight),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(trackWidth / 2f, trackWidth / 2f),
        alpha = 0.9f
    )
}