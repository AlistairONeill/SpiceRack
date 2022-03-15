package uk.co.alistaironeill.spicerack.slot

import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome

interface LedGroupSource {
    fun get(slot: Slot): AonOutcome<Set<Led>>
    fun put(led: Led, slot: Slot): UnitOutcome
    fun remove(led: Led): UnitOutcome
    fun get(): AonOutcome<Map<Slot, Set<Led>>>
}