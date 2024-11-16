package com.esoft.emobile.ui.views.tag

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.esoft.emobile.domain.model.Nf

data class PrinterUiState(
    val form: PrintTagForm = PrintTagForm(),
    val nf: MutableState<Nf> = mutableStateOf(Nf())
)