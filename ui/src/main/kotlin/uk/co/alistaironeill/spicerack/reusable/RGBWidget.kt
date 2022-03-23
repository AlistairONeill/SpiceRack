@file:Suppress("FunctionName")

package uk.co.alistaironeill.spicerack.reusable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uk.co.alistaironeill.spicerack.reusable.ColourType.*
import uk.co.alistaironeill.spicerack.spice.RGB
import kotlin.Byte.Companion.MAX_VALUE
import kotlin.Byte.Companion.MIN_VALUE

@Composable
fun RGBWidget(
    initial: RGB,
    set: (RGB) -> Unit
) {
    val rgb = remember { mutableStateOf(initial) }

    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Visualisation(rgb.value)
        ColourSlider(RED, rgb)
        ColourSlider(GREEN, rgb)
        ColourSlider(BLUE, rgb)
        ControlRow(
            rgb.value != initial,
            { rgb.value = initial },
            { set(rgb.value) }
        )
    }
}

@Composable
private fun Visualisation(colour: RGB) {
    Surface(
        Modifier.size(100.dp),
        shape = CircleShape,
        color = colour.toColor()
    ) {

    }
}

private enum class ColourType(val dark: Color, val vibrant: Color) {
    RED(
        Color(127, 0, 0),
        Color(255, 0, 0)
    ),
    GREEN(
        Color(0, 127, 0),
        Color(0, 255, 0)
    ),
    BLUE(
        Color(0, 0, 127),
        Color(0, 0, 255)
    )
}


@Composable
private fun ColourSlider(
    type: ColourType,
    colour: MutableState<RGB>
) {
    Slider(
        colour[type].toFloat(),
        { value -> colour[type] = value.toInt().toByte() },
        valueRange = MIN_VALUE.toFloat() .. MAX_VALUE.toFloat(),
        colors = SliderDefaults.colors(
            thumbColor = colour.only(type).toColor(),
            activeTrackColor = type.dark,
            inactiveTrackColor = type.vibrant
        )
    )
}

private operator fun MutableState<RGB>.get(type: ColourType) =
    value.run {
        when (type) {
            RED -> red
            GREEN -> green
            BLUE -> blue
        }
    }

private fun MutableState<RGB>.only(type: ColourType) =
    value.run {
        when (type) {
            RED -> copy(green = MIN_VALUE, blue = MIN_VALUE)
            GREEN -> copy(red = MIN_VALUE, blue = MIN_VALUE)
            BLUE -> copy(red = MIN_VALUE, green = MIN_VALUE)
        }
    }

private operator fun MutableState<RGB>.set(type: ColourType, byte: Byte) {
    value = value.run {
        when (type) {
            RED -> copy(red = byte)
            GREEN -> copy(green = byte)
            BLUE -> copy(blue = byte)
        }
    }
}

private fun RGB.toColor(): Color =
    Color(
        red.unsigned(),
        green.unsigned(),
        blue.unsigned()
    )

private fun Byte.unsigned(): Int = this - MIN_VALUE.toInt()

@Composable
private fun ControlRow(
    enabled: Boolean,
    reset: () -> Unit,
    set: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ImageButton(
            "Revert",
            Icons.Default.Clear,
            enabled,
            onClick = reset
        )

        ImageButton(
            "Save",
            Icons.Default.Send,
            enabled,
            onClick = set
        )
    }
}