package com.esoft.emobile.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.esoft.emobile.data.repository.PreferenceRepository
import com.esoft.emobile.support.BluetoothConnectionHandler
import com.sewoo.jpos.printer.ZPLPrinter
import com.sewoo.port.android.BluetoothPort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    application: Application,
    private val activationRepository: PreferenceRepository
) : AndroidViewModel(application) {

    // Bluetooth
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val bluetoothPort: BluetoothPort = BluetoothPort.getInstance()
    private val connectionHandler = BluetoothConnectionHandler(viewModelScope)
    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    // State Management
    val availableDevices = mutableStateOf<List<BluetoothDevice>>(emptyList())
    val pairedDevices = mutableStateOf<List<BluetoothDevice>>(emptyList())
    val isBluetoothEnabled = mutableStateOf(false)
    val isSearching = mutableStateOf(false)
    val isPrinterConnected = mutableStateOf(false)
    val selectedDevice = mutableStateOf<BluetoothDevice?>(null)

    val zplPrinter = ZPLPrinter()

    private var isReceiverRegistered = false

    private val bluetoothReceiver = createBluetoothReceiver()

    init {
        setupBluetoothPort()
        initializeBluetoothState()
        registerBluetoothReceiver()
        attemptAutoConnect()
    }

    // Configurações iniciais
    private fun setupBluetoothPort() {
        bluetoothPort.SetMacFilter(false)
    }

    private fun initializeBluetoothState() {
        isBluetoothEnabled.value = bluetoothAdapter?.isEnabled == true
        listPairedDevices()
    }

    private fun createBluetoothReceiver() = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> handleBluetoothStateChange(intent)
                BluetoothDevice.ACTION_FOUND -> handleDeviceFound(intent)
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> isSearching.value = false
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> handleBondStateChange(intent)
                BluetoothDevice.ACTION_ACL_CONNECTED -> handleDeviceConnected(intent)
                BluetoothDevice.ACTION_ACL_DISCONNECTED -> handleDeviceDisconnected(intent)
            }
        }
    }

    // ==============================
    // Métodos Bluetooth
    // ==============================

    @SuppressLint("MissingPermission")
    fun startDiscovery() {
        if (hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            availableDevices.value = emptyList()
            if (!isSearching.value) {
                isSearching.value = true
                bluetoothAdapter?.startDiscovery()
                Timber.i("BluetoothViewModel", "Iniciando descoberta de dispositivos Bluetooth.")
            } else {
                Timber.i("BluetoothViewModel", "Descoberta já em andamento.")
            }
        } else {
            Timber.e("BluetoothViewModel", "Permissão de escaneamento não concedida.")
        }
    }

    @SuppressLint("MissingPermission")
    fun pairDevice(device: BluetoothDevice) {
        if (hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            try {
                val result = device.createBond()
                if (result) {
                    Timber.i(
                        "BluetoothViewModel",
                        "Solicitação de pareamento enviada para: ${device.name}"
                    )
                } else {
                    Timber.e(
                        "BluetoothViewModel",
                        "Falha ao solicitar pareamento com: ${device.name}"
                    )
                }
            } catch (e: Exception) {
                Timber.e("BluetoothViewModel", "Erro ao parear dispositivo: ${e.message}")
            }
        } else {
            Timber.e("BluetoothViewModel", "Permissão de pareamento Bluetooth não concedida.")
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice) {
        if (selectedDevice.value == device && isPrinterConnected.value) {
            Timber.i("BluetoothViewModel", "Já conectado ao dispositivo: ${device.name}")
            return
        }

        viewModelScope.launch {
            try {
                val bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID)
                bluetoothAdapter?.cancelDiscovery()

                if (bluetoothPort.isValidAddress(device.address)) {
                    bluetoothPort.connect(device)
                    if (bluetoothPort.isConnected) {
                        handleSuccessfulConnection(device)
                    }
                }
            } catch (e: IOException) {
                Timber.e("BluetoothViewModel", "Erro ao conectar ao dispositivo: ${e.message}")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleSuccessfulConnection(device: BluetoothDevice) {
        connectionHandler.startHandler()
        isPrinterConnected.value = true
        selectedDevice.value = device
        Timber.i("BluetoothViewModel", "Conexão estabelecida com ${device.name}")
        monitorConnection()
    }

    private fun monitorConnection() {
        viewModelScope.launch(Dispatchers.IO) {
            while (bluetoothPort.isConnected) {
                delay(1000)
            }
            handleDisconnection()
        }
    }

    private fun handleDisconnection() {
        connectionHandler.stopHandler()
        isPrinterConnected.value = false
        selectedDevice.value = null
        Timber.i("BluetoothViewModel", "Dispositivo desconectado.")
    }

    @SuppressLint("MissingPermission")
    private fun listPairedDevices() {
        if (hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            bluetoothAdapter?.bondedDevices?.let { devices ->
                pairedDevices.value = devices.filter { isImagingDevice(it) }
            }
        } else {
            Timber.e("BluetoothViewModel", "Permissão para listar dispositivos não concedida.")
        }
    }

    // ==============================
    // Handlers de eventos
    // ==============================

    private fun handleBluetoothStateChange(intent: Intent) {
        val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
        isBluetoothEnabled.value = state == BluetoothAdapter.STATE_ON
        Timber.i(
            "BluetoothViewModel",
            "Bluetooth ${if (isBluetoothEnabled.value) "ativado" else "desativado"}"
        )
    }

    private fun handleDeviceFound(intent: Intent) {
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        device?.takeIf { isImagingDevice(it) && availableDevices.value.none { d -> d.address == it.address } }?.let {
            availableDevices.value = availableDevices.value.toMutableList().apply { add(it) }
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleBondStateChange(intent: Intent) {
        val device =
            intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE) ?: return
        when (device.bondState) {
            BluetoothDevice.BOND_BONDED -> {
                Timber.i("BluetoothViewModel", "Dispositivo pareado: ${device.name}")
                updateDeviceLists(device, isPaired = true)
            }

            BluetoothDevice.BOND_NONE -> {
                Timber.i("BluetoothViewModel", "Pareamento removido: ${device.name}")
                updateDeviceLists(device, isPaired = false)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleDeviceConnected(intent: Intent) {
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        device?.let { Timber.i("BluetoothViewModel", "Conectado ao dispositivo: ${it.name}") }
    }

    @SuppressLint("MissingPermission")
    private fun handleDeviceDisconnected(intent: Intent) {
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        device?.let { Timber.i("BluetoothViewModel", "Desconectado do dispositivo: ${it.name}") }
        handleDisconnection()
    }

    private fun updateDeviceLists(device: BluetoothDevice, isPaired: Boolean) {
        val updatedPaired = pairedDevices.value.toMutableList()
        val updatedAvailable = availableDevices.value.toMutableList()

        if (isPaired) {
            if (!updatedPaired.any { it.address == device.address }) updatedPaired.add(device)
            updatedAvailable.removeAll { it.address == device.address }
        } else {
            updatedPaired.removeAll { it.address == device.address }
            if (!updatedAvailable.any { it.address == device.address }) updatedAvailable.add(device)
        }

        pairedDevices.value = updatedPaired
        availableDevices.value = updatedAvailable
    }

    // ==============================
    // Utilitários
    // ==============================

    private fun hasPermission(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getApplication<Application>().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    @SuppressLint("MissingPermission")
    private fun isImagingDevice(device: BluetoothDevice): Boolean {
        return device.bluetoothClass?.majorDeviceClass == BluetoothClass.Device.Major.IMAGING
    }

    private fun registerBluetoothReceiver() {
        if (!isReceiverRegistered) {
            val filter = IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
                addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            }
            getApplication<Application>().registerReceiver(bluetoothReceiver, filter)
            isReceiverRegistered = true
        }
    }

    fun checkAccessStatus(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            activationRepository
                .isLogged().collect {
                    if (isActive) {
                        onResult(it)
                    }
                }
        }
    }

    private fun attemptAutoConnect() {
        viewModelScope.launch(Dispatchers.IO) {
            pairedDevices.value.forEach { device ->
                if (device.bondState == BluetoothDevice.BOND_BONDED && !isPrinterConnected.value) {
                    Timber.i("BluetoothViewModel", "Tentando auto-conectar ao dispositivo: ${device.name}")
                    connectToDevice(device)
                    delay(5000) // Pequeno atraso para evitar múltiplas tentativas simultâneas
                    if (isPrinterConnected.value) {
                        Timber.i("BluetoothViewModel", "Conexão automática bem-sucedida com: ${device.name}")
                        return@launch
                    }
                }
            }
        }
    }

    override fun onCleared() {
        if (isReceiverRegistered) {
            getApplication<Application>().unregisterReceiver(bluetoothReceiver)
            isReceiverRegistered = false
        }
        connectionHandler.stopHandler()
        super.onCleared()
    }
}
