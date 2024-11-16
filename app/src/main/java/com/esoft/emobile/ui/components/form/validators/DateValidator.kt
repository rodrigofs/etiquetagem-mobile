package com.esoft.emobile.ui.components.form.validators

import com.esoft.emobile.ui.components.form.Validator
import java.util.*

class DateValidator(minDateTime: () -> Long, errorText: String? = null) : Validator<Date?>(
    validate = {
        (it?.time ?: -1) >= minDateTime()
    },
    errorText = errorText ?: "Este campo deve ser preenchido."
)