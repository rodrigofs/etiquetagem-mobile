package com.esoft.emobile.ui.views.tag.partials

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp)) // Clip to make the inside content also respect the rounded corners // Optional: Background color of the Box
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.LightGray),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = modifier.padding(4.dp)
                )
            }

            Column(
                modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, end = 16.dp, start = 16.dp, bottom = 6.dp)
            ) {
                content()
            }
        }
    }
}

@Preview(name = "InfoCard")
@Composable
private fun PreviewInfoCard() {
    InfoCard(title = "Title") {
        Text("Content")
    }
}