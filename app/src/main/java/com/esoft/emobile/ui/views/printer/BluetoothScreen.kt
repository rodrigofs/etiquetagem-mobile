package com.esoft.emobile.ui.views.printer

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.esoft.emobile.support.PermissionManager
import com.esoft.emobile.ui.MainActivityViewModel
import com.esoft.emobile.ui.customization.ScreenBase

@SuppressLint("MissingPermission")
@Composable
fun BluetoothScreen(
    modifier: Modifier = Modifier,
    permissionManager: PermissionManager,
    bluetoothViewModel: MainActivityViewModel,
    onTagPrint: () -> Unit = {},
    navController: NavController

) {

    val pairedDevices by bluetoothViewModel.pairedDevices
    val availableDevices by bluetoothViewModel.availableDevices
    val isBluetoothEnabled by bluetoothViewModel.isBluetoothEnabled
    val isSearching by bluetoothViewModel.isSearching

    var hasBluetoothPermission by remember { mutableStateOf(false) }
    var hasLocationPermission by remember { mutableStateOf(false) }

    RequestLocationPermissions(permissionManager) {
        hasLocationPermission = it.hasLocationPermission
    }

    RequestBluetoothPermissions(permissionManager) {
        hasBluetoothPermission = it.hasBluetoothPermission
    }

    LaunchedEffect(key1 = bluetoothViewModel.selectedDevice.value) {
        if (bluetoothViewModel.selectedDevice.value != null) {
            onTagPrint()
        }
    }

    ScreenBase(title = "Conectar Impressora") {
        if (isBluetoothEnabled) {
            if (isSearching) {
                Text(
                    text = "Procurando dispositivos...",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(text = "Dispositivos Pareados", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(8.dp))

            if (pairedDevices.isEmpty()) {
                Text(text = "Nenhuma impressora pareada encontrada.")
            } else {
                pairedDevices.forEach { device ->
                    BluetoothDeviceItem(
                        onClick = { bluetoothViewModel.connectToDevice(device) },
                        device = device,
                        status = "Pareado"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Dispositivos Disponíveis", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            if (availableDevices.isEmpty()) {
                Text(text = "Nenhuma impressora disponível encontrada.")
            } else {
                availableDevices.forEach { device ->
                    BluetoothDeviceItem(
                        onClick = { bluetoothViewModel.pairDevice(device) },
                        device = device,
                        status = "Não Pareado"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (pairedDevices.isEmpty())
                Button(onClick = { bluetoothViewModel.startDiscovery() }) {
                    Text("Buscar Dispositivos")
                }
        } else {
            Text(
                text = "Bluetooth está desativado. Por favor, ative o Bluetooth.",

                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun BluetoothDeviceItem(
    modifier: Modifier = Modifier,
    device: BluetoothDevice,
    onClick: () -> Unit = {},
    status: String
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row {
            Column {
                Text(
                    text = "Nome: ${device.name ?: "Desconhecido"}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Endereço: ${device.address}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(text = "Status: $status", style = MaterialTheme.typography.bodySmall)
            }
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Composable
private fun RequestLocationPermissions(
    permissionManager: PermissionManager,
    onPermissionsResult: (PermissionsLocationState) -> Unit
) {
    var hasLocationPermission by remember { mutableStateOf(false) }

    permissionManager.RequestLocationPermission {
        hasLocationPermission = true
        onPermissionsResult(PermissionsLocationState(hasLocationPermission))
    }
}

@Composable
private fun RequestBluetoothPermissions(
    permissionManager: PermissionManager,
    onPermissionsResult: (PermissionsBluetoothState) -> Unit
) {
    var hasBluetoothPermission by remember { mutableStateOf(false) }

    permissionManager.RequestBluetoothPermission {
        hasBluetoothPermission = true
        onPermissionsResult(PermissionsBluetoothState(hasBluetoothPermission))
    }
}

private data class PermissionsLocationState(
    val hasLocationPermission: Boolean
)

private data class PermissionsBluetoothState(
    val hasBluetoothPermission: Boolean
)