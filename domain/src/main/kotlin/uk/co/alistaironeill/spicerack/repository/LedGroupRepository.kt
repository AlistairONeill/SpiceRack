package uk.co.alistaironeill.spicerack.repository

import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.Led
import uk.co.alistaironeill.spicerack.model.Slot

interface LedGroupRepository {
    fun get(slot: Slot) : AonOutcome<Set<Led>>
    fun put(slot: Slot, leds: Set<Led>) : UnitOutcome
    fun get() : AonOutcome<Map<Slot, Set<Led>>>
}