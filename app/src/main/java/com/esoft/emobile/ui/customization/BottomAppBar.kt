package com.esoft.emobile.ui.customization

import androidx.compose.foundation.border
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.esoft.emobile.navigation.AppDestination

class BottomAppBarItem(
    val label: String,
    val icon: ImageVector,
    val destination: AppDestination
)

@Composable
fun BottomAppBar(
    item: BottomAppBarItem,
    modifier: Modifier = Modifier,
    items: List<BottomAppBarItem> = emptyList(),
    onItemChange: (BottomAppBarItem) -> Unit = {}
) {

    NavigationBar(
        modifier.border(.5.dp, Color.Gray),
        containerColor = MaterialTheme.colorScheme.background,

    ) {
        items.forEach {
            val label = it.label
            val icon = it.icon
            NavigationBarItem(
                colors = NavigationBarItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    selectedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    disabledIconColor = Color.White,
                    disabledTextColor = Color.White,
                ),
                icon = { Icon(icon, contentDescription = label, tint = if (item.label == label) Color.White else Color.Gray) },
                label = { Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp) },
                selected = item.label == label,
                onClick = {
                    onItemChange(it)
                }
            )
        }
    }
}