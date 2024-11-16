package com.rodrigofs.etiquetaskotlin.ui.screens.access

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esoft.emobile.data.repository.PreferenceRepository
import com.esoft.emobile.domain.model.Preference
import com.esoft.emobile.ui.views.access.AccessUiState
import com.rodrigofs.etiquetaskotlin.network.UnitService
import com.rodrigofs.etiquetaskotlin.network.VehicleService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AccessViewModel @Inject constructor(
    private val configurationRepository: PreferenceRepository,
    private val unitService: UnitService,
    private val vehicleService: VehicleService,
) :
    ViewModel() {

    private val _uiState: MutableStateFlow<AccessUiState> =
        MutableStateFlow(AccessUiState())

    val uiState get() = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch(Dispatchers.Main) {
            val acronym = configurationRepository.acronym()
            acronym.let {
                withContext(Dispatchers.Main) {
                    _uiState.value.form.acronym.state.value = it.first()
                    if (it.first().isNotEmpty()) _uiState.value.acronymLoaded.value = true
                }
            }
        }
    }

    fun access() = viewModelScope.launch {

        _uiState.value.form.validate(true)

        if (_uiState.value.form.isValid) {

            val acronym = _uiState.value.form.acronym.state.value ?: ""
            val plate = _uiState.value.form.plate.state.value ?: ""

            val lacronym = withContext(Dispatchers.IO) {
                val lacronym = unitService.verify(acronym)
                lacronym.onFailure {
                    withContext(Dispatchers.Main) {
                        _uiState.value.form.acronym.apply {
                            errorText.add(it.message ?: "OOO")
                            isValid.value = false
                        }
                    }
                }

                lacronym
            }

            val lplate = withContext(Dispatchers.IO) {
                val lplate = vehicleService.verify(plate)

                lplate.onFailure {
                    withContext(Dispatchers.Main) {
                        _uiState.value.form.plate.apply {
                            errorText.add(it.message ?: "OOO")
                            isValid.value = false
                        }
                    }
                }

                lplate
            }

            if (lacronym.isSuccess && lplate.isSuccess) {
                configurationRepository.saveActivation(
                    Preference(
                        acronym = acronym,
                        plate = plate
                    )
                )
            }
        }

    }
}