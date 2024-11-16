package com.esoft.emobile.ui.customization.inputs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.esoft.emobile.ui.components.form.fields.TextField
import com.esoft.emobile.ui.components.form.validators.CTeModel57Validator
import com.esoft.emobile.ui.components.form.validators.NFeModel55Validator
import com.esoft.emobile.ui.views.tag.PrintTagForm

@Composable
fun ChaveAcessoInput(
    modifier: Modifier = Modifier,
    type: String = "",
    form: PrintTagForm,
    focusRequester: FocusRequester = FocusRequester(),
    onBarcodeScanner: () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {

        TextField(
            modifier = modifier
                .padding(bottom = 8.dp)
                .focusRequester(focusRequester),
            label = if (type == "NFE") "NF-e" else "CT-e",
            form = form,
            fieldState = form.accessKey.apply {

                if (type.isNotEmpty()) {
                    validators.removeAll(form.accessKey.validators)
                    if (type == "NFE")
                        validators.add(NFeModel55Validator())
                    if (type == "CTE")
                        validators.add(CTeModel57Validator())

                }
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        onBarcodeScanner()
                    },
                    imageVector = Icons.Outlined.QrCodeScanner,
                    contentDescription = "Ler código de barras"
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        ).Field()
    }
}