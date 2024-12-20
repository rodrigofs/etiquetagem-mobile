package com.esoft.emobile.ui.components.form.validators

import com.esoft.emobile.ui.components.form.Validator

class IsEqualValidator<T>(expectedValue: () -> T, errorText: String? = null) : Validator<T>(
    validate = {
        it == expectedValue()
    },
    errorText = errorText ?: "This field's value is not as expected."
)