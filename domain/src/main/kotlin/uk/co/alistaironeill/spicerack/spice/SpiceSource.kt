package uk.co.alistaironeill.spicerack.spice

import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome

interface SpiceSource {
    fun get(id: SpiceId): AonOutcome<Spice>
    fun get(name: SpiceName): AonOutcome<Spice>
    fun create(name: SpiceName): AonOutcome<Spice>
    fun addAlias(id: SpiceId, name: SpiceName): UnitOutcome
    fun removeAlias(id: SpiceId, name: SpiceName): UnitOutcome
    fun remove(id: SpiceId): UnitOutcome
}