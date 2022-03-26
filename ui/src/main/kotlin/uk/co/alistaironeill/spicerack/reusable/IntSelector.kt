@file:Suppress("FunctionName")

package uk.co.alistaironeill.spicerack.reusable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign

@Composable
fun IntSelector(
    modifier: Modifier,
    value: Int,
    range: IntRange,
    set: (Int) -> Unit
) {
    if (value !in range) throw IllegalArgumentException("$value not in $range")

    Row(
        modifier,
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Arrow(
            Modifier.fillMaxHeight()
                .aspectRatio(1f),
            Icons.Default.KeyboardArrowLeft,
            "Decrease",
            if (value > range.first) {
                { set(value - 1) }
            } else null
        )

        Text(
            value.toString(),
            textAlign = TextAlign.Center
        )

        Arrow(
            Modifier.fillMaxHeight()
                .aspectRatio(1f),
            Icons.Default.KeyboardArrowRight,
            "Increase",
            if (value < range.last) {
                { set(value + 1) }
            } else null
        )
    }
}

@Composable
private fun Arrow(
    modifier: Modifier,
    icon: ImageVector,
    contentDescription: String,
    onClick: (() -> Unit)?
) {
    Icon(
        icon,
        contentDescription,
        modifier
            .clickable { onClick?.invoke() },
        LocalContentColor.current.copy(alpha = LocalContentAlpha.current * if (onClick == null) 0.5f else 1f)
    )
}