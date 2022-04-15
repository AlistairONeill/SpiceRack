@file:Suppress("FunctionName")

package uk.co.alistaironeill.spicerack.leds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.unit.dp
import uk.co.alistaironeill.spicerack.error.orAlert
import uk.co.alistaironeill.spicerack.reusable.IntSelector
import uk.co.alistaironeill.spicerack.reusable.NiceRadio
import uk.co.alistaironeill.spicerack.slot.Led
import uk.co.alistaironeill.spicerack.slot.Led.Strip.ONE
import uk.co.alistaironeill.spicerack.slot.Led.Strip.ZERO
import uk.co.alistaironeill.spicerack.slot.LedGroupSource
import uk.co.alistaironeill.spicerack.slot.Slot

@Composable
fun LedScreen(source: LedGroupSource) {
    //TODO: Figure out a nicer way of triggering the Compose refreshes
    val i = remember { mutableStateOf(0) }
    val led = remember { mutableStateOf(Led(ZERO, Led.Index(0))) }
    val refresh: () -> Unit = { i.value += 1 }
    i.value
    source.get().orAlert { null }?.let { data ->
        Row(
            Modifier.fillMaxWidth()
        ) {
            LedSelector(
                Modifier.weight(1f),
                led.value
            ) {
                led.value = it
                refresh()
            }

            Visualisation(
                Modifier.weight(1f),
                data.entries
                    .singleOrNull { it.value.contains(led.value) }
                    ?.key
            ) {
                source.add(it, led.value)
                refresh()
            }
        }
    }
}

@Composable
fun LedSelector(
    modifier: Modifier,
    led: Led,
    set: (Led) -> Unit
) {
    Column(
        modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NiceRadio(
            Modifier.fillMaxWidth(0.5f),
            Led.Strip.values().toList(),
            led.strip,
            {
                when (it) {
                    ZERO -> "First Strip"
                    ONE -> "Second Strip"
                }
            },
            { set(led.copy(strip = it)) }
        )

        Spacer(Modifier.size(48.dp))

        IntSelector(
            Modifier.fillMaxWidth(0.5f)
                .height(48.dp),
            led.index.value.toInt() - Byte.MIN_VALUE,
            0 until 240
        ) {
            set(led.copy(index = Led.Index((it + Byte.MIN_VALUE).toByte())))
        }
    }
}

private const val extraneousWeight = 5f
private const val borderWeight = 1f
private const val slotWeight = 10f

@Composable
private fun Visualisation(
    modifier: Modifier,
    highlighted: Slot?,
    set: (Slot) -> Unit
) {
    Column(
        modifier
            .aspectRatio(1f)
    ) {
        Spacer(Modifier.weight(slotWeight))
        ExtraneousRow(Modifier.weight(extraneousWeight))
        BorderRow(Modifier.weight(borderWeight))
        (0 until 5).forEach { y ->
            SlotRow(
                Modifier.weight(slotWeight),
                Slot.Index(y),
                highlighted,
                set
            )
            BorderRow(Modifier.weight(borderWeight))
        }
        ExtraneousRow(Modifier.weight(extraneousWeight))
        Spacer(Modifier.weight(slotWeight))
    }
}

@Composable
private fun ExtraneousRow(modifier: Modifier) {
    Row(
        modifier
            .fillMaxWidth()
    ) {
        val base = Modifier.fillMaxHeight()
        Spacer(base.weight(slotWeight))
        Spacer(base.weight(extraneousWeight))
        Border(base.weight(borderWeight))
        repeat(5) {
            Spacer(base.weight(slotWeight))
            Border(base.weight(borderWeight))
        }
        Spacer(base.weight(extraneousWeight))
        Spacer(base.weight(slotWeight))
    }
}

@Composable
private fun BorderRow(modifier: Modifier) {
    Row(
        modifier.fillMaxWidth()
    ) {
        val base = Modifier.fillMaxHeight()
        Spacer(base.weight(slotWeight))
        repeat(5) {
            Border(base.weight(slotWeight))
        }
        repeat(6) {
            Border(base.weight(borderWeight))
        }
        Spacer(base.weight(slotWeight))
    }
}

@Composable
private fun SlotRow(
    modifier: Modifier,
    y: Slot.Index,
    highlighted: Slot?,
    set: (Slot) -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
    ) {
        val base = Modifier.fillMaxHeight()
        Spacer(base.weight(slotWeight))
        Spacer(base.weight(extraneousWeight))
        Border(base.weight(borderWeight))
        (0 until 5)
            .map(Slot::Index)
            .map { x -> Slot(x, y) }
            .forEach { slot ->
                Surface(
                    base.weight(slotWeight)
                        .clickable { set(slot) },
                    color = if (slot == highlighted) Yellow else Transparent
                ) { }
                Border(base.weight(borderWeight))
            }
        Spacer(base.weight(extraneousWeight))
        Spacer(base.weight(slotWeight))
    }
}

@Composable
private fun Border(modifier: Modifier) {
    Surface(modifier, color = Black) { }
}
