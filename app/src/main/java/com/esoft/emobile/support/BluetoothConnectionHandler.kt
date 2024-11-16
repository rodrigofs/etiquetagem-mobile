package com.esoft.emobile.support

import com.sewoo.request.android.RequestHandler
import kotlinx.coroutines.*
import timber.log.Timber

class BluetoothConnectionHandler(private val scope: CoroutineScope) {
    private var job: Job? = null

    /**
     * Inicia o handler Bluetooth se ele não estiver ativo.
     */
    fun startHandler() {
        if (isRunning()) {
            Timber.d("BluetoothConnectionHandler: Já está ativo, ignorando startHandler.")
            return
        }

        job = scope.launch(Dispatchers.IO) {
            Timber.d("BluetoothConnectionHandler: Inicializando RequestHandler.")
            val requestHandler = RequestHandler()
            try {
                runInterruptible {
                    val thread = Thread(requestHandler)
                    thread.start()
                    thread.join()
                    Timber.d("BluetoothConnectionHandler: startHandler executado com sucesso.")
                }
            } catch (e: Exception) {
                Timber.e(e, "BluetoothConnectionHandler: Erro ao executar startHandler.")
            }
        }
    }

    /**
     * Para o handler Bluetooth se ele estiver ativo.
     */
    fun stopHandler() {
        if (!isRunning()) {
            Timber.d("BluetoothConnectionHandler: Já está parado, ignorando stopHandler.")
            return
        }

        job?.cancel()
        job = null
        Timber.d("BluetoothConnectionHandler: stopHandler executado.")
    }

    /**
     * Verifica se o handler Bluetooth está em execução.
     */
    private fun isRunning(): Boolean {
        return job?.isActive == true
    }
}
