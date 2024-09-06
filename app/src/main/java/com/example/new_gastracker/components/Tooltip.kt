package com.example.gastracker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.new_gastracker.screens.GasType
import com.example.new_gastracker.ui.theme.Orange

@Composable
fun Tooltip(
    isVisible: Boolean,
    position: Offset?,
    size: Size?,
    gasType: GasType?,
    onClose: () -> Unit
) {
    if (isVisible && position != null && size != null && gasType != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
                .clickable(
                    indication = null, // Disable ripple effect
                    interactionSource = remember { MutableInteractionSource() } // No interaction source
                ) {
                    onClose()
                }
        ) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = (position.x + size.width / 2).toInt() - 100,
                            y = (position.y - 40).toInt()
                        )
                    }
                    .background(Orange, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .clickable(
                        indication = null, // Disable ripple effect for tooltip
                        interactionSource = remember { MutableInteractionSource() } // No interaction source
                    ) {
                        /* Prevent tooltip from closing when clicked */
                    }
            ) {
                Text(
                    text = "Preço anterior: €${gasType.previousPrice}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}