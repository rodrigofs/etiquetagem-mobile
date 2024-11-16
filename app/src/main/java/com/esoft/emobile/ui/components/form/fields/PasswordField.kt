package com.esoft.emobile.ui.components.form.fields

import android.annotation.SuppressLint
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.esoft.emobile.ui.components.form.Field
import com.esoft.emobile.ui.components.form.FieldState
import com.esoft.emobile.ui.components.form.Form
import com.esoft.emobile.ui.components.form.components.TextFieldComponent


class PasswordField(
    label: String,
    form: Form,
    modifier: Modifier? = Modifier,
    fieldState: FieldState<String?>,
    isVisible: Boolean = true,
    isEnabled: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) : Field<String>(
    label = label,
    form = form,
    fieldState = fieldState,
    isVisible = isVisible,
    isEnabled = isEnabled,
    modifier = modifier,
    keyboardActions = keyboardActions,
    keyboardOptions = keyboardOptions,
    formatter = null
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

        var passwordVisible by remember { mutableStateOf(false) }
        TextFieldComponent(
            modifier = modifier ?: Modifier,
            isEnabled = isEnabled,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            onChange = {
                this.onChange(it, form)
            },
            label = label,
            text = formatter?.invoke(value.value) ?: (value.value ?: ""),
            hasError = fieldState.hasError(),
            errorText = fieldState.errorText,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            }
        )
    }
}
