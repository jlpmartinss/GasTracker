package com.example.new_gastracker.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.new_gastracker.R
import com.example.new_gastracker.ui.theme.Grey
import com.example.new_gastracker.ui.theme.LessWhite


@Composable
fun GasPriceCard(
    gasType: String,
    previousPrice: Double,
    currentPrice: Double,
    onClick: (Offset, Size) -> Unit
) {
    val shape = RoundedCornerShape(32.dp)
    val borderWidth = 0.5.dp
    var cardPosition by remember { mutableStateOf(Offset(0f, 0f)) }
    var cardSize by remember { mutableStateOf(Size.Zero) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(shape)
            .padding(borderWidth)
            .clickable {
                onClick(cardPosition, cardSize)
            }
            .onGloballyPositioned { coordinates ->
                cardPosition = coordinates.positionInRoot()
                cardSize = coordinates.size.toSize()
            },
        colors = CardDefaults.cardColors(
            containerColor = Grey
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "⛽ $gasType",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "€${currentPrice}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(text = "/litro", color = LessWhite)

                val (painterId, colorFilter) = when {
                    currentPrice > previousPrice -> {
                        R.drawable.arrowup to ColorFilter.tint(Color.Red)
                    }
                    currentPrice < previousPrice -> {
                        R.drawable.arrow_down to ColorFilter.tint(Color.Green)
                    }
                    else -> {
                        R.drawable.equal to ColorFilter.tint(Color.White)
                    }
                }
                Image(
                    painter = painterResource(painterId),
                    contentDescription = "Arrow",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(25.dp),
                    colorFilter = colorFilter
                )
            }
        }
    }
}