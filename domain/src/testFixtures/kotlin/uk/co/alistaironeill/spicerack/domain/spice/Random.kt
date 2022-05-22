package uk.co.alistaironeill.spicerack.domain.spice

import uk.co.alistaironeill.spicerack.distinctRandoms
import uk.co.alistaironeill.spicerack.domain.colour.random
import uk.co.alistaironeill.spicerack.model.Spice
import uk.co.alistaironeill.spicerack.spice.RGB
import java.util.*

fun Spice.Name.Companion.random() = Spice.Name(UUID.randomUUID().toString())
fun Spice.Name.Companion.randoms() = distinctRandoms(Spice.Name.Companion::random)