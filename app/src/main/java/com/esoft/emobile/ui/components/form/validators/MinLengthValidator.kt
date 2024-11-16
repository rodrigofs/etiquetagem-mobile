package com.esoft.emobile.ui.components.form.validators

import com.esoft.emobile.ui.components.form.Validator

class MinLengthValidator(minLength: Int, errorText: String? = null) : Validator<String?>(
    validate = {
        (it?.length ?: -1) >= minLength
    },
    errorText = errorText ?: "${minLength} caracteres é o tamanho mínimo permitido"
)