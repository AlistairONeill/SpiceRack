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