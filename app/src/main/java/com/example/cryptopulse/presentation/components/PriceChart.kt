package com.example.cryptopulse.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cryptopulse.domain.model.PricePoint
import com.example.cryptopulse.presentation.theme.PriceDown
import com.example.cryptopulse.presentation.theme.PriceUp
import com.example.cryptopulse.util.NumberFormatUtils

data class ChartTimeRange(val label: String, val days: String)

val chartTimeRanges = listOf(
    ChartTimeRange("1D", "1"),
    ChartTimeRange("7D", "7"),
    ChartTimeRange("30D", "30"),
    ChartTimeRange("90D", "90"),
    ChartTimeRange("1Y", "365")
)

@Composable
fun PriceChart(
    prices: List<PricePoint>,
    selectedRange: ChartTimeRange,
    onRangeSelected: (ChartTimeRange) -> Unit,
    currency: String,
    modifier: Modifier = Modifier
) {
    var selectedPoint by remember { mutableStateOf<PricePoint?>(null) }

    // Determine if price went up or down
    val isPositive = if (prices.size >= 2) prices.last().price >= prices.first().price else true
    val lineColor by animateColorAsState(
        targetValue = if (isPositive) PriceUp else PriceDown,
        animationSpec = tween(500),
        label = "chartColor"
    )
    val gradientTop = lineColor.copy(alpha = 0.3f)
    val gradientBottom = lineColor.copy(alpha = 0.0f)

    // Animate chart appearance
    val progress by animateFloatAsState(
        targetValue = if (prices.size >= 2) 1f else 0f,
        animationSpec = tween(800),
        label = "chartProgress"
    )

    Column(modifier = modifier) {
        // Selected point display
        if (selectedPoint != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = NumberFormatUtils.formatPrice(selectedPoint!!.price, currency),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = java.text.SimpleDateFormat("MMM d, yyyy HH:mm", java.util.Locale.getDefault())
                        .format(java.util.Date(selectedPoint!!.timestamp)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Chart
        if (prices.size >= 2) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(horizontal = 8.dp)
            ) {
                val crosshairColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(prices) {
                            detectTapGestures { offset ->
                                val index = ((offset.x / size.width) * (prices.size - 1))
                                    .toInt()
                                    .coerceIn(0, prices.size - 1)
                                selectedPoint = prices[index]
                            }
                        }
                        .pointerInput(prices) {
                            detectHorizontalDragGestures(
                                onDragEnd = { selectedPoint = null },
                                onHorizontalDrag = { change, _ ->
                                    val index = ((change.position.x / size.width) * (prices.size - 1))
                                        .toInt()
                                        .coerceIn(0, prices.size - 1)
                                    selectedPoint = prices[index]
                                }
                            )
                        }
                ) {
                    val width = size.width
                    val height = size.height
                    val paddingV = 8f

                    val minPrice = prices.minOf { it.price }
                    val maxPrice = prices.maxOf { it.price }
                    val priceRange = if (maxPrice - minPrice > 0) maxPrice - minPrice else 1.0

                    // Only draw up to 'progress' fraction of points
                    val visibleCount = (prices.size * progress).toInt().coerceAtLeast(2)

                    val points = prices.take(visibleCount).mapIndexed { index, point ->
                        val x = (index.toFloat() / (prices.size - 1)) * width
                        val y = height - paddingV - ((point.price - minPrice) / priceRange).toFloat() * (height - 2 * paddingV)
                        Offset(x, y)
                    }

                    // Smooth Bezier path
                    val linePath = Path()
                    val fillPath = Path()

                    if (points.isNotEmpty()) {
                        linePath.moveTo(points.first().x, points.first().y)
                        fillPath.moveTo(points.first().x, height)
                        fillPath.lineTo(points.first().x, points.first().y)

                        for (i in 1 until points.size) {
                            val prev = points[i - 1]
                            val curr = points[i]
                            val cx = (prev.x + curr.x) / 2
                            linePath.cubicTo(cx, prev.y, cx, curr.y, curr.x, curr.y)
                            fillPath.cubicTo(cx, prev.y, cx, curr.y, curr.x, curr.y)
                        }

                        fillPath.lineTo(points.last().x, height)
                        fillPath.close()
                    }

                    // Gradient fill
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(gradientTop, gradientBottom),
                            startY = points.minOfOrNull { it.y } ?: 0f,
                            endY = height
                        )
                    )

                    // Line
                    drawPath(
                        path = linePath,
                        color = lineColor,
                        style = Stroke(width = 3f, cap = StrokeCap.Round)
                    )

                    // Crosshair for selected point
                    selectedPoint?.let { selected ->
                        val index = prices.indexOf(selected).coerceIn(0, prices.size - 1)
                        if (index >= 0 && index < points.size) {
                            val point = points[index]
                            // Vertical line
                            drawLine(
                                color = crosshairColor,
                                start = Offset(point.x, 0f),
                                end = Offset(point.x, height),
                                strokeWidth = 1f
                            )
                            // Outer glow circle
                            drawCircle(
                                color = lineColor.copy(alpha = 0.2f),
                                radius = 12f,
                                center = point
                            )
                            // Main circle
                            drawCircle(
                                color = lineColor,
                                radius = 6f,
                                center = point
                            )
                            // Inner dot
                            drawCircle(
                                color = Color.White,
                                radius = 3f,
                                center = point
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No chart data available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Time range selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            chartTimeRanges.forEach { range ->
                FilterChip(
                    selected = selectedRange == range,
                    onClick = {
                        selectedPoint = null
                        onRangeSelected(range)
                    },
                    label = {
                        Text(
                            text = range.label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (selectedRange == range) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }
    }
}
