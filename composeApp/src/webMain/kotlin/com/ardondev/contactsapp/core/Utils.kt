package com.ardondev.contactsapp.core

import androidx.compose.ui.graphics.toComposeImageBitmap
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