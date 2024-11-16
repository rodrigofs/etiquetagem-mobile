package com.esoft.emobile.ui.customization.buttons

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.unit.dp

@Composable
fun RefreshIconButton(modifier: Modifier = Modifier,requesting: Boolean,contentDescription: String, onClick: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (requesting) 360f else 0f,
        animationSpec = if (requesting) infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        ) else tween(durationMillis = 300), label = "Processando..."
    )

    IconButton(
        enabled = !requesting,
        onClick = {
            onClick()
        },
        modifier = modifier
            .clip(CircleShape)
            .padding(8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = contentDescription,
            modifier = Modifier.rotate(rotation)
        )
    }
}