package com.esoft.emobile.ui.customization.buttons

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SafeIconButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    delayMillis: Long = 2000L, // Tempo para reabilitar o botão
    content: @Composable () -> Unit
) {
    var isClickable by remember { mutableStateOf(enabled) }
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            if (isClickable) {
                onClick()
                isClickable = false
                scope.launch {
                    delay(delayMillis) // Aguarde antes de reabilitar o botão
                    isClickable = true
                }
            }
        },
        enabled = isClickable,
        content = content
    )
}