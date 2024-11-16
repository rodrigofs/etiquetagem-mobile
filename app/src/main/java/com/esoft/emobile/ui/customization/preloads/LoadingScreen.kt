package com.esoft.emobile.ui.customization.preloads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    AlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp), // Fundo semitransparente,
        onDismissRequest = { },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),

                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .height(16.dp)
                    )

                    Text(
                        style = MaterialTheme.typography.labelSmall,
                        text = "Aguarde estamos processando sua solicitação...",
                        color = Color.Gray
                    )
                }

            }
        },
        confirmButton = {}
    )
}