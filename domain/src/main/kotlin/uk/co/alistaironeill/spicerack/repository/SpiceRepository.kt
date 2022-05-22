package uk.co.alistaironeill.spicerack.repository

import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.Spice

interface SpiceRepository {
    fun put(spice: Spice) : UnitOutcome
    fun get() : AonOutcome<Set<Spice>>
    fun get(id: Spice.Id) : AonOutcome<Spice>
    fun get(name: Spice.Name) : AonOutcome<Set<Spice>>
    fun delete(id: Spice.Id) : UnitOutcome
}