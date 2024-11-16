package com.esoft.emobile.ui.customization.ui.customization

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun PrivacyPolicyText(title: String= "Leia nossa Política de Privacidade") {
    val context = LocalContext.current
    Text(
        text = title,
        color = Color.Blue,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable {
            val privacyPolicyUrl = "https://app-politicas-privacidade.s3.sa-east-1.amazonaws.com/sisgt-entrega/politica-privacidade.html"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl))
            context.startActivity(intent)
        }
    )
}