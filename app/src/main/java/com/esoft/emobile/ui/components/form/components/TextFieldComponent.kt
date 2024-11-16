package com.esoft.emobile.ui.components.form.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onChange: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isEnabled: Boolean = true,
    hasError: Boolean = false,
    maxLength: Int? = null,
    errorText: MutableList<String> = mutableListOf(),
    interactionSource: MutableInteractionSource? = null,
    isReadOnly: Boolean = false,
    focusChanged: ((focus: FocusState) -> Unit)? = null,
    focusRequester: FocusRequester = FocusRequester(),
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(modifier = modifier) {
        OutlinedTextField(

            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    focusChanged?.invoke(it)
                },
            value = text,
            onValueChange = {
                if (maxLength == null) onChange(it)
                if (maxLength != null && it.length <= maxLength) onChange(it)
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            enabled = isEnabled,
            colors = OutlinedTextFieldDefaults.colors(
            ),
            isError = hasError,
            label = { Text(label) },
            readOnly = isReadOnly,
            interactionSource = interactionSource ?: remember { MutableInteractionSource() },
            visualTransformation = visualTransformation,
            placeholder = null,
            singleLine = true,
        )
        if (hasError) {
            Text(
                text = errorText.joinToString("\n"),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = TextStyle.Default.copy(color = MaterialTheme.colorScheme.error)
            )
        }
    }
}