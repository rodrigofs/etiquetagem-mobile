package com.esoft.emobile.ui.components.form.validators

import com.esoft.emobile.ui.components.form.Validator

class NotEmptyValidator<T>(errorText: String? = null) : Validator<T>(
    validate = {
        it != null
    },
    errorText = errorText ?: "Este campo deve ser preenchido"
)