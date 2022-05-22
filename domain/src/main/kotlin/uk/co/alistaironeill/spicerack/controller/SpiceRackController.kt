package uk.co.alistaironeill.spicerack.controller

import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.Spice

interface SpiceRackController {
    fun illuminate(names: Set<Spice.Name>): UnitOutcome
}