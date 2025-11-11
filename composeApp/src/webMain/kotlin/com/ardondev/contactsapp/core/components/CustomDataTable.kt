package com.ardondev.contactsapp.core.components

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
 * @param initialData Listado inicial de los datos.
 * @param columnDefs Listado de [ColumnDef].
 * @param pageSize Número de filas por página.
 */
class DataTableViewModel<T : RowData>(
    private val initialData: List<T>,
    private val columnDefs: List<ColumnDef<T>>,
    val pageSize: Int = 10
) {

    private var _allData by mutableStateOf(initialData)
    val allData: List<T> get() = _allData

    // Para actualizar los datos desde el composable
    fun updateData(newData: List<T>) {
        if (_allData != newData) {
            _allData = newData
        }
    }

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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : RowData> DynamicDataTable(
    data: List<T>,
    columnDefs: List<ColumnDef<T>>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null,
    title: String = "",
    actions: @Composable (RowScope.() -> Unit) = {},
    onRowClick: (T) -> Unit
) {
    val scrollState = rememberScrollState()

    // Inicializar ViewModel
    val viewModel = remember { DataTableViewModel(data, columnDefs) }

    LaunchedEffect(data) {
        viewModel.updateData(data)
    }

    val alpha = if (isLoading) 0.5f else 1f

    Row {

        Column(
            modifier = modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(24.dp)
        ) {

            // 1 :: HEADER

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                // 1.1 :: Título

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black
                    )
                )

                Spacer(Modifier.weight(1f))

                // 1.2 :: Actions
                actions()

            }

            Spacer(Modifier.height(16.dp))

            // 1.3 :: Search

            Row {

                CustomSearchBar(
                    query = viewModel.searchText,
                    onQueryChange = viewModel::updateSearchText
                )

                Spacer(Modifier.width(16.dp))

                CustomButton(
                    outline = true,
                    text = "Filters",
                    leadingIcon = Icons.Rounded.FilterList,
                    onClick = {}
                )

            }

            Spacer(Modifier.height(24.dp))

            // 1 :: CONTENEDOR
            Card(
                shape = MaterialTheme.shapes.large,
                border = BorderStroke(
                    width = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {

                    // 1.2 :: COLUMNAS
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        columnDefs.forEach { col ->
                            Text(
                                text = col.header,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Black
                                ),
                                modifier = Modifier
                                    .weight(col.widthWeight)
                                    .padding(16.dp)
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                // 1.3 :: FILAS
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .alpha(alpha)
                ) {
                    viewModel.visibleData.value.forEach { rowData ->
                        DataRow(
                            rowData = rowData,
                            columnDefs = columnDefs,
                            enabled = !isLoading,
                            onClick = { onRowClick(rowData) }
                        )
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }

                        val visibleCount = viewModel.visibleData.value.size
                        val remainingRows = viewModel.pageSize - visibleCount
                        if (remainingRows > 0) {
                            repeat(remainingRows) {
                                Text("", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(16.dp))
                                HorizontalDivider(
                                    thickness = 0.5.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }

                    }

                }

            }

            // 2:: PAGINACIÓN
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
            ) {

                // 1.4 :: PAGINACIÓN
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val totalItems = viewModel.filteredData.value.size
                    val startItem = ((viewModel.currentPage - 1) * viewModel.pageSize + 1).coerceAtMost(totalItems)
                    val endItem = (startItem + viewModel.visibleData.value.size - 1).coerceAtMost(totalItems)

                    // Estado de la paginación
                    if (error != null) {
                        Text(
                            text = error,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    } else if (isLoading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            CircularProgressIndicator(Modifier.size(18.dp))

                            Spacer(Modifier.width(16.dp))

                            Text(
                                text = "Loading data...",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )

                        }
                    } else {
                        Text(
                            text = if (totalItems > 0) {
                                "Mostrando $startItem - $endItem de $totalItems resultados."
                            } else {
                                "No hay resultados."
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                }

                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = viewModel::goToPrevPage,
                    enabled = viewModel.currentPage > 1 && !isLoading,
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
                    enabled = viewModel.currentPage < viewModel.totalPages.value && !isLoading,
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

        VerticalScrollbar(
            adapter = ScrollbarAdapter(scrollState),
            modifier = Modifier.fillMaxHeight()
        )

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
    columnDefs: List<ColumnDef<T>>,
    enabled: Boolean = true,
    onClick: (T) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .hoverable(interactionSource)
            .pointerHoverIcon(
                icon = PointerIcon.Hand,
                overrideDescendants = true
            )
            .background(
                color = if (isHovered && enabled)
                    MaterialTheme.colorScheme.surface
                else
                    MaterialTheme.colorScheme.surfaceContainerLowest
            )
            .clickable(
                enabled = enabled,
                onClick = { onClick(rowData) }
            )
    ) {
        columnDefs.forEach { col ->
            val cellValue = col.accessor(rowData).toString()
            Text(
                text = cellValue,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .weight(col.widthWeight)
                    .padding(16.dp)
            )
        }
    }
}