package uk.co.alistaironeill.spicerack.domain.colour

import uk.co.alistaironeill.spicerack.distinctRandoms
import uk.co.alistaironeill.spicerack.spice.RGB
import kotlin.random.Random.Default.nextBytes

fun RGB.Companion.random() = nextBytes(3).let { (r, g, b) -> RGB(r, g, b) }

fun RGB.Companion.randoms() = distinctRandoms(RGB.Companion::random)