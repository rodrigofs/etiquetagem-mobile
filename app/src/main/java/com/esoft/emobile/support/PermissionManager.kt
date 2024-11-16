package com.esoft.emobile.support

import android.Manifest
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class PermissionManager(private val activity: ComponentActivity) {

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun RequestCameraPermission(onPermissionGranted: () -> Unit) {
        val cameraPermissionState = rememberMultiplePermissionsState(
            permissions = listOf(Manifest.permission.CAMERA)
        )
        val showDialog = remember { mutableStateOf(true) }

        if (showDialog.value && !cameraPermissionState.allPermissionsGranted) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text("Permissão de Câmera Necessária") },
                text = {
                    Text("Para continuar, precisamos acessar a câmera do seu dispositivo. Isso nos permitirá escanear ler o código de barras de NF-e.")
                },
                confirmButton = {
                    Button(onClick = {
                        cameraPermissionState.launchMultiplePermissionRequest()
                        showDialog.value = false
                    }) {
                        Text("Permitir")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog.value = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        LaunchedEffect(cameraPermissionState.allPermissionsGranted) {
            if (cameraPermissionState.allPermissionsGranted) {
                onPermissionGranted()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun RequestLocationPermission(onPermissionGranted: () -> Unit) {
        val locationPermissionState = rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
        val showDialog = remember { mutableStateOf(true) }

        if (showDialog.value && !locationPermissionState.allPermissionsGranted) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text("Permissão de Localização Necessária") },
                text = {
                    Text("Precisamos acessar a sua localização para registrar com precisão os pontos de coleta, e a encontrar mais facilmente a impressora de etiquetas.")
                },
                confirmButton = {
                    Button(onClick = {
                        locationPermissionState.launchMultiplePermissionRequest()
                        showDialog.value = false
                    }) {
                        Text("Permitir")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog.value = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        LaunchedEffect(locationPermissionState.allPermissionsGranted) {
            if (locationPermissionState.allPermissionsGranted) {
                onPermissionGranted()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun RequestBluetoothPermission(onPermissionGranted: () -> Unit) {

        val bluetoothPermissionState = rememberMultiplePermissionsState(
            permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Para Android 12 (API 31) e superior
                listOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADVERTISE
                )
            } else {
                // Para versões anteriores ao Android 12
                listOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN
                )
            }
        )


        val showDialog = remember { mutableStateOf(true) }

        if (showDialog.value && !bluetoothPermissionState.allPermissionsGranted) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text("Permissão de Bluetooth Necessária") },
                text = {
                    Text("Este aplicativo requer permissões de Bluetooth para realizar a conexão com dispositivos externos, como impressoras. Por favor, conceda a permissão para que possamos continuar fornecendo todas as funcionalidades.")
                },
                confirmButton = {
                    Button(onClick = {
                        bluetoothPermissionState.launchMultiplePermissionRequest()
                        showDialog.value = false
                    }) {
                        Text("Permitir")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog.value = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        LaunchedEffect(bluetoothPermissionState.allPermissionsGranted) {
            if (bluetoothPermissionState.allPermissionsGranted) {
                onPermissionGranted()
            }
        }
    }
}
