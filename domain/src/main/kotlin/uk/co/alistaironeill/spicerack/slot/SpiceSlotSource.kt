package uk.co.alistaironeill.spicerack.slot

import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.spice.SpiceId

interface SpiceSlotSource {
    fun put(slot: Slot, id: SpiceId) : UnitOutcome
    fun get(id: SpiceId) : AonOutcome<Slot>
    fun get(slot: Slot) : AonOutcome<SpiceId?>
}