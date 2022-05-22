package uk.co.alistaironeill.spicerack.source

import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.Spice
import uk.co.alistaironeill.spicerack.spice.RGB

interface SpiceSource {
    fun get(): AonOutcome<Set<Spice>>
    fun get(id: Spice.Id): AonOutcome<Spice>
    fun get(name: Spice.Name): AonOutcome<Spice>
    fun create(name: Spice.Name): AonOutcome<Spice>
    fun addAlias(id: Spice.Id, name: Spice.Name): UnitOutcome
    fun removeAlias(id: Spice.Id, name: Spice.Name): UnitOutcome
    fun rename(id: Spice.Id, name: Spice.Name): UnitOutcome
    fun setColour(id: Spice.Id, colour: RGB): UnitOutcome
    fun delete(id: Spice.Id): UnitOutcome
}