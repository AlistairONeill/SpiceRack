package uk.co.alistaironeill.spicerack.domain.colour

import uk.co.alistaironeill.spicerack.domain.error.AonOutcome
import uk.co.alistaironeill.spicerack.domain.error.UnitOutcome

interface RGBSource {
    fun get(colour: Colour) : AonOutcome<RGB>
    fun set(colour: Colour, rgb: RGB) : UnitOutcome
}