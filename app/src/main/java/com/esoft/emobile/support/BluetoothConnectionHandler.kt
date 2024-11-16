package com.esoft.emobile.support

import com.sewoo.request.android.RequestHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runInterruptible
import timber.log.Timber

class BluetoothConnectionHandler(private val scope: CoroutineScope) {
    private var job: Job? = null

    fun startHandler() {
        job?.cancel()  // Cancela qualquer Job existente antes de começar um novo.
        job = scope.launch(Dispatchers.IO) {
            val requestHandler = RequestHandler()
            runInterruptible {
                val thread = Thread(requestHandler)
                thread.start()
                thread.join()
                Timber.d("BluetoothConnectionHandler: startHandler executado.")
            }
        }
    }

    fun stopHandler() {
        job?.cancel()  // Cancela o Job atual.
        Timber.d("BluetoothConnectionHandler: stopHandler executado.")
    }
}
