package com.ardondev.contactsapp.core

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.files.File
import org.w3c.files.get

/**
 * Abre el explorador de archivos y llama a la función de callback con el archivo seleccionado.
 * @param onFileSelected Función a llamar con el archivo (File) seleccionado.
 * @param accept String de tipos de archivos aceptados.
 */
fun openFileExplorer(accept: String, onFileSelected: (File?) -> Unit) {
    // 1. Crear un elemento de input de tipo 'file' en memoria.
    val input = document.createElement("input") as HTMLInputElement
    input.type = "file"
    input.accept = accept // Define los tipos de archivos aceptados.

    // 2. Establecer el manejador de eventos 'change'.
    input.onchange = { event ->
        val fileList = input.files
        val file = fileList?.get(0) // Obtener el primer archivo seleccionado.
        onFileSelected(file)
        null // El manejador de eventos debe devolver Unit o Nothing?.
    }

    // 3. Simular un clic en el input para abrir el explorador.
    input.click()
}