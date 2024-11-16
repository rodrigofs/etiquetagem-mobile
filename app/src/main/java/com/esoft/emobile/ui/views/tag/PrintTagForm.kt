package com.esoft.emobile.ui.views.tag

import androidx.compose.runtime.mutableStateOf
import com.esoft.emobile.ui.components.form.FieldState
import com.esoft.emobile.ui.components.form.Form
import com.esoft.emobile.ui.components.form.FormField
import com.esoft.emobile.ui.components.form.validators.NotEmptyValidator

class PrintTagForm : Form() {
    override fun self(): Form {
        return this
    }

    @FormField
    val accessKey = FieldState(
        state = mutableStateOf<String?>(null),
        validators = mutableSetOf(
            NotEmptyValidator(),
        ),
    )

    @FormField
    val volumes = FieldState(
        state = mutableStateOf<String?>(null),
        validators = mutableSetOf(
            NotEmptyValidator(),
        )
    )
}