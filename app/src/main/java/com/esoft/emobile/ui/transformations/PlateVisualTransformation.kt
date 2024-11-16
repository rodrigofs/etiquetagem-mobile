package com.esoft.emobile.ui.transformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PlateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Transformar todo o texto em letras maiúsculas
        val uppercaseText = text.text.uppercase()
        val uppercaseAnnotatedString = AnnotatedString(uppercaseText)

        // Definir o mapeamento de offset, que é necessário, mas neste caso pode ser simples
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = offset
            override fun transformedToOriginal(offset: Int): Int = offset
        }

        // Retornar o texto transformado e o mapeamento de offset
        return TransformedText(uppercaseAnnotatedString, offsetMapping)
    }
}

