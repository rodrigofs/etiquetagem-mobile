package com.esoft.emobile.ui.views.access

import androidx.compose.runtime.mutableStateOf
import com.esoft.emobile.ui.components.form.FieldState
import com.esoft.emobile.ui.components.form.Form
import com.esoft.emobile.ui.components.form.FormField
import com.esoft.emobile.ui.components.form.validators.MaxLengthValidator
import com.esoft.emobile.ui.components.form.validators.NotEmptyValidator
import com.esoft.emobile.ui.customization.PlacaField

class AccessForm : Form(), PlacaField {
    override fun self(): Form {
        return this
    }

    @FormField
    val acronym = FieldState(
        state = mutableStateOf<String?>(null),
        validators = mutableSetOf(
            NotEmptyValidator(),
        ),
    )

    @FormField
    override val plate = FieldState(
        state = mutableStateOf<String?>(null),
        validators = mutableSetOf(
            NotEmptyValidator(),
        )
    )
}