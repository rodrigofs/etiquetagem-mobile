package com.esoft.emobile.ui.components.form.validators

import com.esoft.emobile.ui.components.form.Validator

class MaxLengthValidator(maxLength: Int, errorText: String? = null) : Validator<String?>(
    validate = {
        (it?.length ?: -1) <= maxLength
    },
    errorText = errorText ?: "${maxLength} caracteres é o tamanho máximo permitido"
)