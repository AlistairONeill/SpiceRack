package uk.co.alistaironeill.spicerack.domain.spice

import uk.co.alistaironeill.spicerack.distinctRandoms
import uk.co.alistaironeill.spicerack.domain.colour.random
import uk.co.alistaironeill.spicerack.spice.RGB
import uk.co.alistaironeill.spicerack.spice.Spice
import uk.co.alistaironeill.spicerack.spice.SpiceId
import uk.co.alistaironeill.spicerack.spice.SpiceName
import java.util.*

fun SpiceName.Companion.random() = SpiceName(UUID.randomUUID().toString())
fun SpiceName.Companion.randoms() = distinctRandoms(SpiceName.Companion::random)