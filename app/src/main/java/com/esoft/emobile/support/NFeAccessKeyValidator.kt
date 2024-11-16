package com.esoft.emobile.support

class NFeAccessKeyValidator {

    fun isValid(accessKey: String): Boolean {
        if (accessKey.length != 44) return false
        if (!accessKey.all { it.isDigit() }) return false

        val ufCode = accessKey.substring(0, 2).toInt()
        if (ufCode !in 11..53) return false

        val model = accessKey.substring(20, 22)
        if (model != "55" && model != "65") return false

        return checkDigitVerifier(accessKey)
    }

    private fun checkDigitVerifier(key: String): Boolean {
        val weights = intArrayOf(2, 3, 4, 5, 6, 7, 8, 9)
        var sum = 0

        // Itera sobre os primeiros 43 dígitos da chave, da direita para a esquerda
        for (i in 0 until 43) {
            // Os pesos são aplicados da direita para a esquerda, repetindo de 2 a 9
            val weight = weights[(43 - 1 - i) % weights.size]
            sum += (key[i].toString().toInt() * weight)
        }

        val remainder = sum % 11
        val calculatedDV = if (remainder < 2) 0 else 11 - remainder

        // O último dígito da chave é o dígito verificador
        val actualDV = key[43].toString().toInt()

        return calculatedDV == actualDV
    }
}