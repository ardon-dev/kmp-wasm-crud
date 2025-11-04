import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ardondev.contactsapp.core.simpleVerticalScrollbar

/**
 * Componente genérico para mostrar contenido con cabecera y pie de página.
 *
 * @param title El texto que se mostrará en el encabezado.
 * @param onDismissRequest Una lambda para manejar el cierre (ej. clic en la 'X').
 * @param content El contenido principal scrollable (ej. un formulario).
 * @param footer El contenido fijo en la parte inferior (ej. acciones).
 */
@Composable
fun ContentCard(
    title: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
    footer: @Composable BoxScope.() -> Unit
) {
    val scrollState = rememberScrollState()

    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Column(
            modifier = modifier
                .width(intrinsicSize = IntrinsicSize.Max)
                .heightIn(max = 800.dp)
                .fillMaxHeight(0.9f)
        ) {

            // Header -->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
            ) {
                // Título
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .align(Alignment.Center)
                )

                // Botón de cierre
                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cerrar"
                    )
                }
            }
            // <-- Header

            HorizontalDivider()

            // Content -->
            Column(
                content = content,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .simpleVerticalScrollbar(
                        state = scrollState,
                        color = Color.Gray
                    )
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            )
            // <-- Content

            HorizontalDivider()

            // Footer -->
            Box(
                content = footer,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            )
            // <-- Footer
        }
    }
}