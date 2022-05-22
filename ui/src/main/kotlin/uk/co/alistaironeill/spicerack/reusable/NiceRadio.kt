@file:Suppress("FunctionName")

package uk.co.alistaironeill.spicerack.reusable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun <T> NiceRadio(
    modifier: Modifier,
    options: List<T>,
    selected: T,
    label: (T) -> String,
    set: (T) -> Unit
) {
    if (options.size < 2) {
        throw NotImplementedError()
    }
    Row(modifier) {
        Panel(
            Modifier.weight(1f),
            RoundedCornerShape(
                CornerSize(50),
                CornerSize(0),
                CornerSize(0),
                CornerSize(50)
            ),
            options.first(),
            selected,
            label,
            set
        )

        options
            .drop(1)
            .dropLast(1)
            .forEach { option ->
                Panel(
                    Modifier.weight(1f),
                    RectangleShape,
                    option,
                    selected,
                    label,
                    set
                )
            }

        Panel(
            Modifier.weight(1f),
            RoundedCornerShape(
                CornerSize(0),
                CornerSize(50),
                CornerSize(50),
                CornerSize(0)
            ),
            options.last(),
            selected,
            label,
            set
        )
    }
}

@Composable
private fun <T> Panel(
    modifier: Modifier,
    shape: Shape,
    option: T,
    selected: T,
    label: (T) -> String,
    set: (T) -> Unit
) {
    val isSelected = option == selected
    Surface(
        color = if (isSelected) {
            MaterialTheme.colors.onSurface
        } else {
            Color.Transparent
        },
        contentColor = if (isSelected) {
            MaterialTheme.colors.onPrimary
        } else {
            Color.LightGray
        },
        shape = shape,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary
        ),
        modifier = modifier
            .clickable { if (!isSelected) set(option) }
    ) {
        Text(
            text = label(option),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(8.dp)
        )
    }
}
