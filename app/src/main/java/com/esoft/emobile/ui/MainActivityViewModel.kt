package com.esoft.emobile.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.esoft.emobile.data.repository.PreferenceRepository
import com.esoft.emobile.support.BluetoothConnectionHandler
import com.sewoo.jpos.printer.ZPLPrinter
import com.sewoo.port.android.BluetoothPort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    application: Application,
    private val activationRespository: PreferenceRepository
) : AndroidViewModel(application) {

    private val bluetoothReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when (action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val state =
                        intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                    when (state) {
                        BluetoothAdapter.STATE_OFF -> {
                            isBluetoothEnabled.value = false
                            Timber.i("BluetoothViewModel", "Bluetooth desativado")
                        }

                        BluetoothAdapter.STATE_ON -> {
                            isBluetoothEnabled.value = true
                            Timber.i("BluetoothViewModel", "Bluetooth ativado")
                        }
                    }
                }

                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device != null && isImagingDevice(device) && !pairedDevices.value.contains(
                            device
                        )
                    ) {
                        availableDevices.value = availableDevices.value.toMutableList().apply {
                            if (!contains(device)) add(device)
                        }
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    isSearching.value = false
                }

                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        when (it.bondState) {
                            BluetoothDevice.BOND_BONDED -> {
                                Timber.i("BluetoothViewModel", "Dispositivo pareado: ${it.name}")
                                listPairedDevices() // Atualiza a lista de dispositivos pareados
                                availableDevices.value =
                                    availableDevices.value.toMutableList().apply {
                                        remove(it)
                                    }
                            }

                            BluetoothDevice.BOND_NONE -> {
                                Timber.i(
                                    "BluetoothViewModel",
                                    "Pareamento falhou ou foi removido para o dispositivo: ${it.name}"
                                )
                                availableDevices.value =
                                    availableDevices.value.toMutableList().apply {
                                        if (!contains(it)) add(it)
                                    }
                                listPairedDevices() // Atualiza a lista de dispositivos pareados
                            }
                        }
                    }
                }

                BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        selectedDevice.value = it
                        Timber.i("BluetoothViewModel", "Conectado ao dispositivo: ${it.name}")
                    }
                }

                BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        selectedDevice.value = null
                        bluetoothPort.disconnect()
                        Timber.i("BluetoothViewModel", "Desconectado do dispositivo: ${it.name}")
                    }
                }
            }
        }
    }

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    val availableDevices = mutableStateOf<List<BluetoothDevice>>(emptyList())
    val pairedDevices = mutableStateOf<List<BluetoothDevice>>(emptyList())
    val isBluetoothEnabled = mutableStateOf(false)
    val isSearching = mutableStateOf(false)
    val selectedDevice = mutableStateOf<BluetoothDevice?>(null)
    private val connectionHandler = BluetoothConnectionHandler(viewModelScope)
    val bluetoothPort: BluetoothPort = BluetoothPort.getInstance()
    val zplPrinter =  ZPLPrinter();

    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    init {
        bluetoothPort.SetMacFilter(false)
        checkBluetoothState()
        listPairedDevices()
        registerBluetoothReceiver()
        startDiscovery()
    }

    // Registrar BroadcastReceiver para dispositivos disponíveis e eventos de Bluetooth
    private fun registerBluetoothReceiver() {
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        getApplication<Application>().registerReceiver(bluetoothReceiver, filter)
    }

    @SuppressLint("MissingPermission")
    private fun isImagingDevice(device: BluetoothDevice): Boolean {
        val deviceClass = device.bluetoothClass
        return deviceClass?.majorDeviceClass == BluetoothClass.Device.Major.IMAGING
    }

    private fun checkBluetoothState() {
        isBluetoothEnabled.value = bluetoothAdapter?.isEnabled == true
    }

    @SuppressLint("MissingPermission")
    private fun listPairedDevices() {
        if (hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            bluetoothAdapter?.bondedDevices?.let { devices ->
                pairedDevices.value = devices.filter { device ->
                    isImagingDevice(device)
                }
            }
        } else {
            Timber.e("BluetoothViewModel", "Permissão para listar dispositivos não concedida.")
        }
    }

    // Iniciar a descoberta de dispositivos disponíveis
    @Suppress("MissingPermission")
    fun startDiscovery() {
        if (hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            isSearching.value = true
            availableDevices.value = emptyList() // Limpar lista anterior
            bluetoothAdapter?.startDiscovery()
        }
    }

    // Função para conectar a um dispositivo
    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice) {
        viewModelScope.launch {
            try {
                val bluetoothSocket: BluetoothSocket? = device.createRfcommSocketToServiceRecord(MY_UUID)
                bluetoothSocket?.let {
                    bluetoothAdapter?.cancelDiscovery()

                    if (bluetoothPort.isValidAddress(it.remoteDevice.address)) {
                        bluetoothPort.connect(it.remoteDevice)
                        connectionHandler.startHandler()
                    }

                    Timber.i("BluetoothViewModel", "Conexão estabelecida com ${device.name}")
                }
            } catch (e: IOException) {
                Timber.e("BluetoothViewModel", "Erro ao conectar ao dispositivo: ${e.message}")
            }
        }
    }

    // Função para parear com um dispositivo
    @Suppress("MissingPermission")
    fun pairDevice(device: BluetoothDevice) {
        if (hasPermission(Manifest.permission.BLUETOOTH_ADMIN)) {
            device.createBond()
        } else {
            Timber.e("BluetoothViewModel", "Permissão para parear com dispositivo não concedida.")
        }
    }

    // Verifica se a permissão foi concedida
    private fun hasPermission(permission: String): Boolean {
        return getApplication<Application>().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCleared() {
        super.onCleared()
        getApplication<Application>().unregisterReceiver(bluetoothReceiver)
        connectionHandler.stopHandler()
    }

    fun checkAccessStatus(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            activationRespository.isLogged().collect {
                if (isActive) {
                    onResult(it)
                }
            }
        }
    }
}