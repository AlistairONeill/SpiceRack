package uk.co.alistaironeill.spicerack.model

import uk.co.alistaironeill.spicerack.distinctRandoms
import uk.co.alistaironeill.spicerack.spice.RGB
import java.util.*
import kotlin.random.Random

fun Spice.Name.Companion.random() = Spice.Name(UUID.randomUUID().toString())
fun Spice.Name.Companion.randoms() = distinctRandoms(Spice.Name.Companion::random)

fun Spice.Companion.random() = Spice(
    Spice.Id.mint(),
    Spice.Name.random(),
    List(3) { Spice.Name.random() }.toSet(),
    RGB.random()
)
fun Spice.Companion.randoms() = distinctRandoms(Spice.Companion::random)

fun RGB.Companion.random() = Random.nextBytes(3).let { (r, g, b) -> RGB(r, g, b) }
fun RGB.Companion.randoms() = distinctRandoms(RGB.Companion::random)

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

private fun Byte.Companion.random() : Byte = Random.nextBytes(1)[0]

fun Led.Index.Companion.random() : Led.Index = Led.Index(Byte.random())