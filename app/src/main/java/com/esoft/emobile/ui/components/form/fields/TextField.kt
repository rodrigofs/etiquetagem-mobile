package com.esoft.emobile.ui.components.form.fields

import android.annotation.SuppressLint
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.esoft.emobile.ui.components.form.Field
import com.esoft.emobile.ui.components.form.FieldState
import com.esoft.emobile.ui.components.form.Form
import com.esoft.emobile.ui.components.form.components.TextFieldComponent
import java.util.*

class TextField(
    label: String,
    form: Form,
    modifier: Modifier? = Modifier,
    fieldState: FieldState<String?>,
    isVisible: Boolean = true,
    isEnabled: Boolean = true,
    formatter: ((raw: String?) -> String)? = null,
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
) : Field<String>(
    label = label,
    form = form,
    fieldState = fieldState,
    isVisible = isVisible,
    isEnabled = isEnabled,
    modifier = modifier,
    formatter = formatter,
    maxLength = maxLength,
    keyboardOptions = keyboardOptions,
    visualTransformation = visualTransformation,
    trailingIcon = trailingIcon,
    leadingIcon = leadingIcon,
    keyboardActions = keyboardActions
) {

    /**
     * Returns a composable representing the DateField / Picker for this field
     */
    @SuppressLint("NotConstructor")
    @Composable
    override fun Field() {
        this.updateComposableValue()
        if (!isVisible) {
            return
        }

        TextFieldComponent(
            modifier = modifier ?: Modifier,
            isEnabled = isEnabled,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            maxLength = maxLength,
            onChange = {
                this.onChange(it, form)
            },
            label = label,
            text = formatter?.invoke(value.value) ?: (value.value ?: ""),
            hasError = fieldState.hasError(),
            errorText = fieldState.errorText,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon,
        )
    }
}
