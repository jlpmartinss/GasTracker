package com.example.new_gastracker.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gastracker.components.Tooltip
import com.example.new_gastracker.GasViewModel
import com.example.new_gastracker.components.GasPriceCard
import com.example.new_gastracker.ui.theme.LessWhite
import com.example.new_gastracker.ui.theme.Orange
import com.google.accompanist.insets.LocalWindowInsets


data class GasType(
    val name: String,
    val previousPrice: Double,
    val currentPrice: Double
)

@Composable
fun GasInfoScreen(gasViewModel: GasViewModel = viewModel()) {
    val gasPrices by gasViewModel.gasResponse.collectAsState()
    var selectedGasType by remember { mutableStateOf<GasType?>(null) }
    var tooltipPosition by remember { mutableStateOf<Offset?>(null) }
    var tooltipSize by remember { mutableStateOf<Size?>(null) }
    var showTooltip by remember { mutableStateOf(false) }

    // Mock data for testing
    val gasTypes = listOf(
        GasType("Gasolina 95", 1.50, 1.60),
        GasType("Gasolina 98", 1.70, 1.80),
        GasType("Gasóleo", 1.40, 1.55),
        GasType("Gasóleo Colorido", 1.30, 1.25),
        GasType("Gasóleo Colorido2", 1.30, 1.30),
        GasType("Gasóleo Colorido3", 1.30, 1.25),
        GasType("Electricidade", 0.20, 0.25)
    )

    val density = LocalDensity.current.density
    val insets = LocalWindowInsets.current
    val bottomPadding = (insets.navigationBars.bottom / density).dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = bottomPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Bem-vindo ao ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.ExtraBold,
                            color = Orange
                        )
                    ) {
                        append("GasTracker!")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
                fontFamily = FontFamily.Monospace,
                fontSize = 40.sp,
                lineHeight = 48.sp, // Adjust the line height for spacing between lines
                color = Color.White
            )

            Text(
                text = "Aqui podes consultar os preços dos combustíveis atualizados da ilha da Madeira!",
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp),
            )

            Text(
                text = "\uD83D\uDD0E Clica num combustível para ver o preço anterior",
                color = LessWhite,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp)
            ) {
                gasPrices?.let { gasPrice ->
                    val gasData = gasPrice.current.gas

                    val gasTypes = listOf(
                        GasType(
                            "Gasolina 95",
                            gasPrice.previous.gas.gasolinaIO95,
                            gasData.gasolinaIO95
                        ),
                        GasType(
                            "Gasolina 98",
                            gasPrice.previous.gas.gasolinaIO98,
                            gasData.gasolinaIO98
                        ),
                        GasType(
                            "Gasóleo",
                            gasPrice.previous.gas.gasoleoRodoviario,
                            gasData.gasoleoRodoviario
                        ),
                        GasType(
                            "Gasóleo Colorido",
                            gasPrice.previous.gas.gasoleoColoridoMarcado,
                            gasData.gasoleoColoridoMarcado
                        )
                    )

                    items(gasTypes.size) { index ->
                        val gasType = gasTypes[index]
                        GasPriceCard(
                            gasType = gasType.name,
                            previousPrice = gasType.previousPrice,
                            currentPrice = gasType.currentPrice,
                            onClick = { position, size ->
                                selectedGasType = gasType
                                tooltipPosition = position
                                tooltipSize = size
                                showTooltip = true
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                } ?: item {
                    Text(text = "Loading...", color = Color.White)
                }
            }
        }

        Tooltip(
            isVisible = showTooltip,
            position = tooltipPosition,
            size = tooltipSize,
            gasType = selectedGasType,
            onClose = { showTooltip = false }
        )
    }
}