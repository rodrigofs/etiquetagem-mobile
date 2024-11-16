package com.esoft.emobile.ui.components.form.fields

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.esoft.emobile.R
import com.esoft.emobile.ui.components.form.Field
import com.esoft.emobile.ui.components.form.FieldState
import com.esoft.emobile.ui.components.form.Form
import com.esoft.emobile.ui.components.form.components.TextFieldComponent
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeField(
    label: String,
    form: Form,
    modifier: Modifier? = Modifier,
    fieldState: FieldState<LocalTime?>,
    isVisible: Boolean = true,
    isEnabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    formatter: ((raw: LocalTime?) -> String)? = null,
    private val themeResId: Int = 0,
    trailingIcon: @Composable (() -> Unit)? = null
) : Field<LocalTime>(
    label = label,
    form = form,
    fieldState = fieldState,
    isVisible = isVisible,
    isEnabled = isEnabled,
    modifier = modifier,
    imeAction = imeAction,
    formatter = formatter,
    trailingIcon = trailingIcon
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

        val focusRequester = FocusRequester()
        val focusManager = LocalFocusManager.current
        val context = LocalContext.current

        val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val time = if (value.value != null) LocalTime.parse(value.value.toString(), timeFormat) else LocalTime.now()

        val timePickerDialog = TimePickerDialog(
            context,
            { _, hour, minute ->
                this.onChange(LocalTime.of(hour, minute), form)
            },
            time.hour,
            time.minute,
            true,
        )

        timePickerDialog.setOnDismissListener {
            focusManager.clearFocus()
        }

        TextFieldComponent(
            modifier = modifier ?: Modifier,
            isEnabled = isEnabled,
            label = label,
            text = formatter?.invoke(value.value) ?: value.value.toString(),
            hasError = fieldState.hasError(),
            errorText = fieldState.errorText,
            isReadOnly = true,
            focusRequester = focusRequester,
            focusChanged = {
                if (it.isFocused) {
                    timePickerDialog.show()
                }
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_access_time_24),
                    contentDescription = "Selecionar hora",
                    modifier = Modifier
                        .size(20.dp, 20.dp)
                        .clickable {
                            timePickerDialog.show()
                        }
                )
            },
        )
    }
}