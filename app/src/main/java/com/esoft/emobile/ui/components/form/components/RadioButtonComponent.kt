package com.esoft.emobile.ui.components.form.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun <T> RadioButtonComponent(
    label: String,
    value: T? = null,
    selectedValue: T?,
    onClickListener: (T?) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = ((value ?: label) == selectedValue),
                onClick = {
                    onClickListener(value)
                }
            )
    ) {
        androidx.compose.material3.RadioButton(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(alignment = Alignment.CenterVertically),
            selected = ((value ?: label) == selectedValue),
            onClick = {
                onClickListener(value)
            }
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.merge(),
            modifier = Modifier
                .padding(8.dp)
                .align(alignment = Alignment.CenterVertically)
        )
    }
}

@Composable
@Preview
fun RadioButtonPreview() {
    RadioButtonComponent(label = "RadioButton", value = "", onClickListener = {}, selectedValue = "")
}