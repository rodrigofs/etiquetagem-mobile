package com.esoft.emobile.ui.components.form

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class FieldState<T>(
    val state: MutableState<T>,
    val validators: MutableSet<Validator<T>> = mutableSetOf(),
    val errorText: MutableList<String> = mutableListOf(),
    val isValid: MutableState<Boolean?> = mutableStateOf(false),
    val hasChanges: MutableState<Boolean?> = mutableStateOf(false),
    val options: MutableList<T>? = null,
    val optionItemFormatter: ((T?) -> String)? = null,
) {
    fun hasError(): Boolean {
        return isValid.value == false && hasChanges.value == true
    }

    fun selectedOption(): T? {
        if (options == null) {
            return null
        }

        return options.firstOrNull { it == state.value }
    }

    fun selectedOptionText(): String? {
        val selectedOption = selectedOption() ?: return null
        return optionItemFormatter?.invoke(selectedOption) ?: selectedOption.toString()
    }
}