@file:Suppress("FunctionName")

package uk.co.alistaironeill.spicerack.reusable

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ImageButton(
    text: String,
    icon: ImageVector,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick : () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(8.dp),
        enabled = enabled
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
        Text(text = text)
    }
}