package com.esoft.emobile.ui.components.form.fields

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.esoft.emobile.ui.components.form.Field
import com.esoft.emobile.ui.components.form.FieldState
import com.esoft.emobile.ui.components.form.Form
import com.esoft.emobile.ui.components.form.components.CheckboxComponent

class CheckboxField(
    label: String,
    form: Form,
    modifier: Modifier? = Modifier,
    fieldState: FieldState<Boolean?>,
    isVisible: Boolean = true,
    isEnabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    formatter: ((raw: Boolean?) -> String)? = null
) : Field<Boolean>(
    label = label,
    form = form,
    fieldState = fieldState,
    isVisible = isVisible,
    isEnabled = isEnabled,
    modifier = modifier,
    imeAction = imeAction,
    formatter = formatter
) {

    /**
     * Returns a composable representing the DateField / Picker for this field
     */
    @Composable
    override fun Field() {
        this.updateComposableValue()
        if (!isVisible) {
            return
        }
        CheckboxComponent(
            modifier = modifier ?: Modifier,
            checked = fieldState.state.value == true,
            onCheckedChange = {
                this.onChange(it, form)
            },
            label = label,
            hasError = fieldState.hasError(),
            errorText = fieldState.errorText
        )
    }
}
