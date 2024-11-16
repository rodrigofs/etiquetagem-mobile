package com.esoft.emobile.ui.customization.logos

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.esoft.emobile.R



@Composable
fun LogoSisgt(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo Sisgt"
    )

}

@Preview(backgroundColor = 0xFFFF, showBackground = true)
@Composable
fun LogoSisgtPreview() {
    LogoSisgt()
}