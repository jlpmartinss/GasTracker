package com.example.new_gastracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gastracker.components.Tooltip
import com.example.new_gastracker.components.GasPriceCard


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

    val insets = WindowInsets.systemBars


    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(insets)
            .padding(16.dp)
    ) {
        gasPrices?.let { gasPrice ->
            val gasData = gasPrice.current.gas

            val gasTypes = listOf(
                GasType("Gasoline", gasPrice.previous.gas.gasolinaIO95, gasData.gasolinaIO95),
                GasType("Premium Gasoline", gasPrice.previous.gas.gasolinaIO98, gasData.gasolinaIO98),
                GasType("Diesel", gasPrice.previous.gas.gasoleoRodoviario, gasData.gasoleoRodoviario),
                GasType("Coloured Diesel", gasPrice.previous.gas.gasoleoColoridoMarcado, gasData.gasoleoColoridoMarcado)
            )

            gasTypes.forEach { gasType ->
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
        } ?: run {
            androidx.compose.material3.Text(text = "Loading...", color = Color.White)
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



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GasInfoScreen()
}
