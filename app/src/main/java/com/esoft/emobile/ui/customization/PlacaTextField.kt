package com.esoft.emobile.ui.customization

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.esoft.emobile.ui.components.form.FieldState
import com.esoft.emobile.ui.components.form.Form
import com.esoft.emobile.ui.components.form.fields.TextField
import com.esoft.emobile.ui.transformations.PlateVisualTransformation

interface PlacaField {
    val plate: FieldState<String?>
}

@Composable
fun <T: Form> PlacaTextField(
    modifier: Modifier = Modifier,
    form: T,
    onFocusChanged: (FocusState) -> Unit = {},
    focusRequester: FocusRequester,
    isEnabled: Boolean,
    keyboardController: SoftwareKeyboardController?
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { onFocusChanged(it) },
        label = "Placa",
        form = form,
        maxLength = 7,
        fieldState = (form as PlacaField).plate,
        isEnabled = isEnabled,
        visualTransformation = PlateVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusRequester.requestFocus() },
            onDone = { keyboardController?.hide() }
        ),
    ).Field()
}

private fun handleInput(input: String): String {
    return input.filter { it.isLetterOrDigit() }.take(7).uppercase()
}

private fun getKeyboardOptions(text: String?): KeyboardOptions {

    if (text == null) return KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Next)

    return when {
        text.length < 3 -> {
            KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Next)
        }
        text.length == 3 -> {
            KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Next)
        }
        text.length == 4 -> {
            KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Next)
        }
        else -> {
            KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done)
        }
    }
}
