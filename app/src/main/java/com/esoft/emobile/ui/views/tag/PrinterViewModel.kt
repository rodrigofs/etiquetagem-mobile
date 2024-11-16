package com.esoft.emobile.ui.views.tag

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esoft.emobile.data.remote.model.GetTagApiResponse
import com.esoft.emobile.data.remote.model.asDomainModel
import com.esoft.emobile.data.repository.PreferenceRepository
import com.esoft.emobile.domain.model.Nf
import com.esoft.emobile.domain.model.Tag
import com.esoft.emobile.support.LabelPrinterService
import com.esoft.emobile.support.NFeAccessKeyValidator
import com.rodrigofs.etiquetaskotlin.network.TagService
import com.sewoo.jpos.printer.ZPLPrinter
import com.sewoo.port.android.DeviceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PrinterViewModel @Inject constructor(
    private val configuration: PreferenceRepository,
    private val tagApi: TagService
) : ViewModel() {

    private val _uiState: MutableStateFlow<PrinterUiState> =
        MutableStateFlow(PrinterUiState())

    val uiState get() = _uiState.asStateFlow()

    val _acronym = mutableStateOf("")
    val _plate = mutableStateOf("")

    var openDialog by mutableStateOf(false)
    val showCamera = mutableStateOf(false)
    val fetching = mutableStateOf(false)
    val printing = mutableStateOf(false)

//    init {
//        uiState.value.form.accessKey.state.value = "43240407413349000102550000000091931000014636"
//    }

    private val nfeValidator = NFeAccessKeyValidator()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            configuration.acronym().first().also { _acronym.value = it }
            configuration.plate().first().also { _plate.value = it }
        }
    }

    suspend fun clearPlate() = configuration.clearPlate()

    suspend fun fetchTag() = viewModelScope.launch {
        _uiState.value.form.validate(true)

        try {
            if (_uiState.value.form.isValid) {
                val accessKey = _uiState.value.form.accessKey.state.value ?: ""
                val volumes = _uiState.value.form.volumes.state.value ?: ""

                fetching.value = true

                if (!nfeValidator.isValid(accessKey)) {
                    _uiState.value.form.accessKey.errorText.add("Chave de acesso está inválida para NF-e.")
                    _uiState.value.form.accessKey.isValid.value = false
                    return@launch
                }

                if (accessKey.subSequence(
                        20,
                        22
                    ) != "55"
                ) {
                    _uiState.value.form.accessKey.errorText.add("Informe a chave de acesso não é uma NF-e.")
                    _uiState.value.form.accessKey.isValid.value = false
                    return@launch
                }

                if (volumes.isEmpty()) {
                    _uiState.value.form.volumes.errorText.add("Volume deve ser informado.")
                    _uiState.value.form.volumes.isValid.value = false
                    return@launch
                }

                if (volumes.toInt() <= 0) {
                    _uiState.value.form.volumes.errorText.add("Volumes não pode ser zero.")
                    _uiState.value.form.volumes.isValid.value = false
                    return@launch
                }

                val response = withContext(Dispatchers.IO) {
                    val response = tagApi.requestTags(
                        chaveAcesso = accessKey,
                        volumes = volumes,
                        unidade = _acronym.value.uppercase(),
                        placa = _plate.value.uppercase()
                    )

                    response.onFailure { error ->
                        withContext(Dispatchers.Main) {
                            _uiState.value.form.accessKey.errorText.add(error.message.toString())
                            _uiState.value.form.accessKey.isValid.value = false
                        }
                    }

                    response
                }

                if (response.isSuccess) {
                    _uiState.value.nf.value =
                        GetTagApiResponse().asDomainModel(response.getOrThrow())
                }
            }

        } catch (e: Exception) {
            _uiState.value.form.accessKey.errorText.add(e.message.toString())
            _uiState.value.form.accessKey.isValid.value = true
        } finally {
            fetching.value = false
        }
    }

    fun startPrint(tags: List<Tag>, zplPrinter: ZPLPrinter) {
        viewModelScope.launch {
            printing.value = true

            val printerService =  LabelPrinterService(zplPrinter, _acronym.value, _plate.value)

            printerService.printLabels(tags)

            clearForm()
            printing.value = false
        }
    }

    fun clearForm() {
        _uiState.value.form.accessKey.state.value = ""
        _uiState.value.form.volumes.state.value = ""
        _uiState.value.nf.value = Nf()
    }

    fun readBarcode() {
        showCamera.value = true
    }

}
