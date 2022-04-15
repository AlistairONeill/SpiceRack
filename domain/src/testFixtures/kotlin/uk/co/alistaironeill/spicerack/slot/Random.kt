package uk.co.alistaironeill.spicerack.slot

import uk.co.alistaironeill.spicerack.distinctRandoms
import kotlin.random.Random.Default.nextBytes

fun Slot.Companion.random() = Slot(
    Slot.Index.random(),
    Slot.Index.random()
)

fun Slot.Companion.randoms() = distinctRandoms(Slot.Companion::random)

fun Slot.Index.Companion.random() = Slot.Index(Byte.random())

fun Led.Companion.random() =
    Led(
        Led.Strip.values().random(),
        Led.Index.random()
    )

fun Led.Companion.randoms() : Iterable<Led> = distinctRandoms(Led.Companion::random)

private fun Byte.Companion.random() : Byte = nextBytes(1)[0]

fun Led.Index.Companion.random() : Led.Index = Led.Index(Byte.random())