package uk.co.alistaironeill.spicerack.controller

import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.spice.SpiceName

interface SpiceRackController {
    fun illuminate(names: Set<SpiceName>): UnitOutcome
}