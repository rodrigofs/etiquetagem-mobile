package com.esoft.emobile.ui.customization.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RegisterButton(
    modifier: Modifier = Modifier,
    isRegistred: Boolean = false,
    requesting: Boolean = false,
    onClick: () -> Unit
) {
    if (isRegistred)
        return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LoadingButton(
            shape = RoundedCornerShape(20),
            modifier = Modifier.weight(1f),
            loading = requesting,
            enabled = !requesting,
            onClick = onClick
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(5.dp),
                    tint = Color.White,
                    imageVector = Icons.Outlined.Done,
                    contentDescription = "Ponto de entrega"
                )

                Text(text = "Registrar")
            }
        }
    }
}