package com.esoft.emobile.ui.views.access

import androidx.compose.runtime.mutableStateOf

class AccessUiState {
    val form: AccessForm = AccessForm()
    val acronymLoaded = mutableStateOf(false)
}