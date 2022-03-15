package uk.co.alistaironeill.spicerack.domain.colour

import uk.co.alistaironeill.spicerack.colour.RGB
import kotlin.random.Random.Default.nextBytes

fun RGB.Companion.random() = nextBytes(3).let { (r, g, b) -> RGB(r, g, b) }