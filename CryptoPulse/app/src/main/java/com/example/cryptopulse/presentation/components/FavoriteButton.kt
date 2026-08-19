package com.example.cryptopulse.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.cryptopulse.presentation.theme.Gold

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    coinName: String = "coin"
) {
    val tint by animateColorAsState(
        targetValue = if (isFavorite) Gold else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "favorite_color"
    )

    IconButton(
        onClick = onToggle,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
            contentDescription = if (isFavorite) "Remove $coinName from favorites" else "Add $coinName to favorites",
            tint = tint
        )
    }
}
