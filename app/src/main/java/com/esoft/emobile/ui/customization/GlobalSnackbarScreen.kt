package com.esoft.emobile.ui.customization.ui.customization

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GlobalSnackbarScreen(content: @Composable (showSnackbar: (String, String?) -> Unit) -> Unit) {

    val snackbarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(24.dp)
                )
            }
        },
        content = {
            content { message, actionLabel ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message, actionLabel)
                }
            }
        }
    )

}