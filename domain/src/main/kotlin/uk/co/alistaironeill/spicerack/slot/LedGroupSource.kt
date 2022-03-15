package uk.co.alistaironeill.spicerack.slot

import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome

interface LedGroupSource {
    fun get(slot: Slot): AonOutcome<Set<Led>>
    fun add(slot: Slot, led: Led): UnitOutcome
    fun clear(slot: Slot): UnitOutcome
    fun get(): AonOutcome<Map<Slot, Set<Led>>>
}