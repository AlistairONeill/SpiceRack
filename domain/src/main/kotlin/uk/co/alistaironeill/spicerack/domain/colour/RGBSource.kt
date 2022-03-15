package uk.co.alistaironeill.spicerack.domain.colour

import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome

interface RGBSource {
    fun get(colour: Colour) : AonOutcome<RGB>
    fun set(colour: Colour, rgb: RGB) : UnitOutcome
}