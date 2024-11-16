package com.esoft.emobile.ui.customization.buttons

import android.app.Activity
import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.esoft.emobile.ui.components.form.FieldState


@Composable
fun <T> ScannerButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fieldState: FieldState<T>,
    onClick: () -> Unit,
) {

    IconButton(

        enabled = enabled,
        onClick = onClick,
        modifier = modifier
            .fillMaxSize()
            .fillMaxWidth()
            .border(1.dp, if (fieldState.hasError()) MaterialTheme.colorScheme.error else Color.Gray, RoundedCornerShape(5.dp))
    ) {
        Icon(
            tint = if (fieldState.hasError()) MaterialTheme.colorScheme.error else Color.Gray,
            imageVector = Icons.Filled.PhotoCamera,
            contentDescription = "Tirar foto",
            modifier = Modifier.size(35.dp)
        )
    }
}