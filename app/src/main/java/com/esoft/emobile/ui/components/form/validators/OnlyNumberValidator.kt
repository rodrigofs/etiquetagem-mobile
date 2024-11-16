package com.esoft.emobile.ui.components.form.validators

import com.esoft.emobile.ui.components.form.Validator

class OnlyNumberValidator(errorText: String? = null) : Validator<String?>(
    validate = {
        "^[0-9]+$".toRegex().matches("$it")
    },
    errorText = errorText ?: "Somente numeros são permitidos."
)