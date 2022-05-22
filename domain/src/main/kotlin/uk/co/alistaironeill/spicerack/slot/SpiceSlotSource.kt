package uk.co.alistaironeill.spicerack.slot

import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.Spice

interface SpiceSlotSource {
    fun put(slot: Slot, id: Spice.Id) : UnitOutcome
    fun get(id: Spice.Id) : AonOutcome<Slot>
    fun get(slot: Slot) : AonOutcome<Spice.Id?>
}