package com.esoft.emobile.ui.views.printer

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.esoft.emobile.support.PermissionManager
import com.esoft.emobile.ui.MainActivityViewModel
import com.esoft.emobile.ui.customization.ScreenBase
import com.esoft.emobile.ui.customization.buttons.LoadingButton
import com.esoft.emobile.ui.customization.cards.CardTitle
import com.esoft.emobile.ui.theme.green

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

            Spacer(modifier = Modifier.height(8.dp))

            CardTitle(text = "Dispositivos Pareados")

            Spacer(modifier = Modifier.height(8.dp))

            if (pairedDevices.isEmpty()) {
                Text(text = "Nenhuma impressora pareada encontrada.")
            } else {
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp)
                ) {
                    items(items = pairedDevices) { device ->
                        BluetoothDeviceItem(
                            modifier = Modifier.padding(bottom = 12.dp),
                            device = device,
                            status = "Pareado"

                        ) {
                            bluetoothViewModel.connectToDevice(device)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CardTitle(text = "Dispositivos Disponíveis")

            Spacer(modifier = Modifier.height(8.dp))

            if (availableDevices.isEmpty()) {
                Text(text = "Nenhuma impressora disponível encontrada.")
            } else {
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp)
                ) {
                    items(items = availableDevices) { device ->
                        BluetoothDeviceItem(
                            modifier = Modifier.padding(bottom = 12.dp),
                            device = device,
                            status = "Não Pareado"
                        ) {
                            bluetoothViewModel.pairDevice(device)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (pairedDevices.isEmpty())
                LoadingButton(
                    onClick = { bluetoothViewModel.startDiscovery() },
                    loading = isSearching,

                    ) {
                    if (isSearching) {
                        Text("Pesquisando...")
                    } else {
                        Text("Buscar Dispositivos")
                    }
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
    status: String,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(.3f),
                    text = "NOME: ",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    modifier = Modifier.weight(.7f),
                    text = device.name ?: "Desconhecido",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {

                Text(
                    modifier = Modifier.weight(.3f),
                    text = "ENDEREÇO: ",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    modifier = Modifier.weight(.7f),
                    text = device.address,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {

                Text(
                    modifier = Modifier.weight(.3f),
                    text = "STATUS: ",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    modifier = Modifier.weight(.7f),
                    text = status.uppercase(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (status == "Pareado") green else MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
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