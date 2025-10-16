package com.ardondev.contactsapp.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.ceil

/**
 * Tipo genérico para los datos de las filas.
 */
typealias RowData = Any

/**
 * Clase para alojar el contenido de una Columna en una tabla de datos.
 *
 * @param header Título de la columna.
 * @param accessor Función para extraer el valor de la celda.
 * @param isFilterable Permitir filtrar en esta columna.
 * @param widthWeight Peso de la columna.
 */
data class ColumnDef<T : RowData>(
    val header: String,
    val accessor: (T) -> Any,
    val isFilterable: Boolean = true,
    val widthWeight: Float = 1f
)

/**
 * ViewModel para [DynamicDataTable]. Gestiona los datos de la tabla.
 *
 * @param allData Listado de los datos.
 * @param columnDefs Listado de [ColumnDef].
 * @param pageSize Número de filas por página.
 */
class DataTableViewModel<T : RowData>(
    private val allData: List<T>,
    private val columnDefs: List<ColumnDef<T>>,
    val pageSize: Int = 10
) {

    // Valor de búsqueda por texto
    var searchText by mutableStateOf("")
        private set

    // Valor de la página actual
    var currentPage by mutableStateOf(1)
        private set

    // Expone los datos que deben mostrarse (filtrados/paginados)
    val filteredData: State<List<T>> = derivedStateOf {

        val data =
            // Si no se ha ingresado texto de búsqueda se consideran todos los datos
            if (searchText.isBlank()) allData
            // Si se ingresó texto de búsqueda se filtrarán los datos
            else filterData()

        // Si el filtro cambia y la página actual queda fuera del rango, vuelve a la primera página
        val maxPossiblePage = ceil(data.size.toDouble() / pageSize).toInt().coerceAtLeast(1)
        if (currentPage > maxPossiblePage) {
            currentPage = 1
        }

        data
    }

    // Calcular el total de páginas
    val totalPages: State<Int> = derivedStateOf {
        ceil(filteredData.value.size.toDouble() / pageSize).toInt().coerceAtLeast(1)
    }

    // Reúne los datos que serán visibles
    val visibleData: State<List<T>> = derivedStateOf {
        val data = filteredData.value
        if (data.isEmpty()) return@derivedStateOf emptyList()

        val startIndex = (currentPage - 1) * pageSize
        // Asegura que no se exceda el tamaño de la lista
        val endIndex = (startIndex + pageSize).coerceAtMost(data.size)

        // Devuelve el segmento de datos
        if (startIndex >= data.size) {
            emptyList()
        } else {
            data.subList(startIndex, endIndex)
        }
    }

    /**
     * Actualiza el estado de [searchText].
     */
    fun updateSearchText(newText: String) {
        searchText = newText
    }

    /**
     * Filtra por texto los valores de las columnas que tengan habilitada la filtración.
     */
    private fun filterData(): List<T> {
        val lowerCaseQuery = searchText.lowercase()

        // Filtra solo en las columnas marcadas como 'isFilterable'
        val filterableAccessors = columnDefs
            .filter { it.isFilterable }
            .map { it.accessor }

        return allData.filter { row ->
            filterableAccessors.any { accessor ->
                // Convierte el valor de la celda a String para la comparación
                accessor(row).toString().lowercase().contains(lowerCaseQuery)
            }
        }
    }

    /**
     * Cambia a una página específica solo si se encuentra en el total de páginas.
     *
     * @param page Número de página.
     */
    fun goToPage(page: Int) {
        if (page in 1..totalPages.value) {
            currentPage = page
        }
    }

    /**
     * Cambia a la pagina siguiente.
     */
    fun goToNextPage() = goToPage(currentPage + 1)

    /**
     * Cambia a la pagina anterior.
     */
    fun goToPrevPage() = goToPage(currentPage - 1)
}

/**
 * Componente Tabla para mostrar datos.
 *
 * @param data Listado de datos.
 * @param columnDefs Listado de [ColumnDef].
 * @param modifier Modificador para el componente.
 */
@Composable
fun <T : RowData> DynamicDataTable(
    data: List<T>,
    columnDefs: List<ColumnDef<T>>,
    modifier: Modifier = Modifier,
    onFilterClick: (() -> Unit)? = null,
    onRefreshClick: (() -> Unit)? = null,
    onAddClick: (() -> Unit)? = null
) {
    // Inicializar ViewModel
    val viewModel = remember { DataTableViewModel(data, columnDefs) }

    // 1 :: CONTENEDOR
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {

        // 1.1 :: CABECERA
        DataTableHeader(
            searchText = viewModel.searchText,
            onSearchTextChange = viewModel::updateSearchText,
            actions = {
                onFilterClick?.let {
                    DataTableAction(
                        icon = Icons.Filled.FilterList,
                        small = true,
                        onClick = it
                    )
                }
                onRefreshClick?.let {
                    DataTableAction(
                        icon = Icons.Filled.Sync,
                        small = true,
                        onClick = it
                    )
                }
                onAddClick?.let {
                    DataTableAction(
                        icon = Icons.Filled.Add,
                        onClick = it
                    )
                }
            }
        )

        // 1.2 :: COLUMNAS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(16.dp)
        ) {
            columnDefs.forEach { col ->
                Text(
                    text = col.header,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .weight(col.widthWeight)
                )
            }
        }

        // 1.3 :: FILAS
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // Si se está filtrando por texto y no hay resultados se muestra mensaje
            if (viewModel.visibleData.value.isEmpty() && viewModel.searchText.isNotBlank()) {
                item {
                    Text(
                        "No hay resultados para la búsqueda '${viewModel.searchText}'",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                    )
                }
            }
            // Listar los datos como filas
            else {
                items(viewModel.visibleData.value) { rowData ->
                    DataRow(rowData, columnDefs)
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }

            // Espacio en blanco para las filas restantes
//            val visibleCount = viewModel.visibleData.value.size
//            val remainingRows = viewModel.pageSize - visibleCount
//            if (remainingRows > 0) {
//                items(remainingRows) {
//                    Spacer(Modifier.height(40.dp))
//                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
//                }
//            }
        }

        // 1.4 :: PAGINACIÓN
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            val totalItems = viewModel.filteredData.value.size
            val startItem = ((viewModel.currentPage - 1) * viewModel.pageSize + 1).coerceAtMost(totalItems)
            val endItem = (startItem + viewModel.visibleData.value.size - 1).coerceAtMost(totalItems)

            // Estado de la paginación
            Text(
                text = if (totalItems > 0) {
                    "Mostrando $startItem - $endItem de $totalItems resultados."
                } else {
                    "No hay resultados."
                },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )

            // Botones de paginación
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                IconButton(
                    onClick = viewModel::goToPrevPage,
                    enabled = viewModel.currentPage > 1,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                            .rotate(180f)
                    )
                }

                Text(
                    text = "${viewModel.currentPage} / ${viewModel.totalPages.value}",
                    style = MaterialTheme.typography.labelMedium
                )

                IconButton(
                    onClick = viewModel::goToNextPage,
                    enabled = viewModel.currentPage < viewModel.totalPages.value,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                    )
                }
            }
        }

    }

}

/**
 * Componente cabecera para la tabla de datos.
 *
 * @param searchText Valor a de búsqueda actual.
 * @param onSearchTextChange Callback que recupera el nuevo valor de búsqueda.
 * @param actions Fila de componentes diseñada para [DataTableAction].
 */
@Composable
fun DataTableHeader(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    actions: @Composable (RowScope.() -> Unit)
) {

    Row(
        modifier = Modifier
            .padding(16.dp)
    ) {

        // Entrada de texto de búsqueda
        TextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            placeholder = { Text("Buscar...", style = MaterialTheme.typography.bodyMedium) },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Buscar") },
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                errorIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        // Espacio vacío
        Spacer(Modifier.weight(1f))

        // Lista de acciones
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )

    }
}

/**
 * Componente botón para representar una acción en la tabla de datos.
 *
 * @param icon Ícono como un [ImageVector].
 * @param small Determina si el botón debe ser pequeño.
 * @param onClick Callback para manejar el evento click.
 */
@Composable
fun DataTableAction(
    icon: ImageVector,
    small: Boolean = false,
    onClick: () -> Unit = {}
) {
    if (small) {
        SmallFloatingActionButton(
            onClick = onClick,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp,
                focusedElevation = 0.dp
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        }
    } else {
        FloatingActionButton(
            onClick = onClick,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp,
                focusedElevation = 0.dp
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        }
    }
}

/**
 * Componente que representa una fila en la tabla de datos.
 *
 * @param rowData Modelo de datos.
 * @param columnDefs Listado de [ColumnDef].
 */
@Composable
private fun <T : RowData> DataRow(
    rowData: T,
    columnDefs: List<ColumnDef<T>>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        columnDefs.forEach { col ->
            val cellValue = col.accessor(rowData).toString()
            Text(
                text = cellValue,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(col.widthWeight)
                    .padding(16.dp)
            )
        }
    }
}