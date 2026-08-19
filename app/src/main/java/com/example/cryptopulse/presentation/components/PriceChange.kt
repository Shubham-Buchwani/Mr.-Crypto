package com.example.cryptopulse.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cryptopulse.presentation.theme.PriceDown
import com.example.cryptopulse.presentation.theme.PriceDownBackground
import com.example.cryptopulse.presentation.theme.PriceUp
import com.example.cryptopulse.presentation.theme.PriceUpBackground
import com.example.cryptopulse.util.NumberFormatUtils

@Composable
fun PriceChange(
    percentage: Double?,
    modifier: Modifier = Modifier,
    showBackground: Boolean = true
) {
    if (percentage == null) return

    val isPositive = percentage >= 0
    val color = if (isPositive) PriceUp else PriceDown
    val backgroundColor = if (isPositive) PriceUpBackground else PriceDownBackground
    val arrow = if (isPositive) "▲" else "▼"
    val text = NumberFormatUtils.formatPercentage(percentage)

    if (showBackground) {
        Surface(
            color = backgroundColor,
            shape = RoundedCornerShape(6.dp),
            modifier = modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "$arrow $text",
                    color = color,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    } else {
        Text(
            text = "$arrow $text",
            color = color,
            style = MaterialTheme.typography.labelMedium,
            modifier = modifier
        )
    }
}
