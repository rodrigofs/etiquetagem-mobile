package com.esoft.emobile.ui.components.form.validators

import com.esoft.emobile.ui.components.form.Validator

class CTeModel57Validator(errorText: String? = null) : Validator<String?>(
    validate = {
        // Verifica se a chave tem exatamente 44 dígitos e se o modelo é 55
        it?.let { chave ->
            chave.length == 44 && chave.substring(20, 22) == "57"
        } ?: false
    },
    errorText = errorText ?: "Chave de acesso inválida para CT-e."
)