package com.esoft.emobile.ui.transformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class AcronymVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Limita o texto a no máximo 3 caracteres e transforma em letras maiúsculas
        val limitedText = text.text.take(3).uppercase()



        // OffsetMapping simples para manter a transformação sincronizada com a entrada original
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Mantém o offset no limite de até 3 caracteres
                return offset.coerceAtMost(3)
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Mantém o offset no limite de até 3 caracteres
                return offset.coerceAtMost(3)
            }
        }

        return TransformedText(AnnotatedString(limitedText), offsetMapping)
    }
}