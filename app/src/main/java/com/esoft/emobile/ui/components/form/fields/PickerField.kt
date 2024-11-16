package com.esoft.emobile.ui.components.form.fields

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.esoft.emobile.ui.components.form.Field
import com.esoft.emobile.ui.components.form.FieldState
import com.esoft.emobile.ui.components.form.Form
import com.esoft.emobile.ui.components.form.components.SingleSelectDialogComponent
import com.esoft.emobile.ui.components.form.components.TextFieldComponent
import com.esoft.emobile.R

abstract class PickerValue {
    abstract fun searchFilter(query: String): Boolean
}

class PickerField<T : PickerValue>(
    label: String,
    form: Form,
    modifier: Modifier? = Modifier,
    fieldState: FieldState<T?>,
    isVisible: Boolean = true,
    isEnabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    formatter: ((raw: T?) -> String)? = null,
    onClose: () -> Unit = {}
) : Field<T>(
    label = label,
    form = form,
    fieldState = fieldState,
    isVisible = isVisible,
    isEnabled = isEnabled,
    modifier = modifier,
    imeAction = imeAction,
    formatter = formatter,
    onClose = onClose
) {

    @Composable
    override fun Field() {
        this.updateComposableValue()
        if (!isVisible) {
            return
        }

        var isDialogVisible by remember { mutableStateOf(false) }
        val focusRequester = FocusRequester()
        val focusManager = LocalFocusManager.current

        TextFieldComponent(
            modifier = modifier ?: Modifier,
            isEnabled = isEnabled,
            label = label,
            text = fieldState.selectedOptionText() ?: "",
            hasError = fieldState.hasError(),
            errorText = fieldState.errorText,
            isReadOnly = true,
            trailingIcon = {
                Icon(
                    if (isDialogVisible) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    null
                )
            },
            focusRequester = focusRequester,
            focusChanged = {
                isDialogVisible = it.isFocused
            }

        )

        if (isDialogVisible) {
            SingleSelectDialogComponent(
                title = label,
                optionsList = fieldState.options!!,
                optionItemFormatter = fieldState.optionItemFormatter,
                defaultSelected = fieldState.state.value,
                submitButtonText = stringResource(R.string.ok),
                onSubmitButtonClick = {
                    isDialogVisible = false
                    this.onChange(it, form)
                    focusManager.clearFocus()
                    this.onClose()
                },
                onDismissRequest = {
                    isDialogVisible = false
                    focusManager.clearFocus()
                    this.onClose()
                }
            ) { options, query ->
                options.filter { c -> c?.searchFilter(query) == true }
            }
        }
    }

}
