package uk.co.alistaironeill.spicerack.colour

import uk.co.alistaironeill.spicerack.error.NotFound

enum class Colour {
    GREEN
}

operator fun NotFound.Companion.invoke(colour: Colour) = NotFound("Colour", colour.name)