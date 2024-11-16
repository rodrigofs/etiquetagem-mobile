@file:OptIn(ExperimentalMaterial3Api::class)

package com.esoft.emobile.ui.customization

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat

@Composable
fun ScreenBase(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit,
) {
    val scrollState = rememberScrollState()

    val view = LocalView.current

    val density = LocalDensity.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White

                ),
                actions = {
                    // Ícone de ações no canto superior direito
                    var showMenu by remember { mutableStateOf(false) }

                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Menu")
                    }

                    // Menu dropdown que aparece ao clicar no ícone de ações
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Limpar formuário") },
                            onClick = {
                                showMenu = false
                            })

                        DropdownMenuItem(text = { Text(text = "Alterar placa") }, onClick = {
//                            scope.launch {
//                                viewModel.clearPlate()
//                                navigateToAccess()
//                                showMenu = false
//                            }
                        })

                        DropdownMenuItem(
                            text = { Text(text = "Sobre") },
                            onClick = {
                                // navigateToAbout()
                                showMenu = false
                            })

                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .systemBarsPadding().imePadding()
                .padding(
                    bottom = WindowInsetsCompat
                        .toWindowInsetsCompat(
                            view.rootWindowInsets
                        )
                        .getInsets(WindowInsetsCompat.Type.ime()).bottom.toDp(density)
                )
                .verticalScroll(scrollState),
        ) {
            Column(modifier
                .padding(paddingValues)
                .padding(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                content()
            }
        }
    }
}

private fun Int.toDp(density: Density): Dp {
    return (this / density.density).dp
}