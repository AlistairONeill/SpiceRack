package uk.co.alistaironeill.spicerack.source

import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.Led
import uk.co.alistaironeill.spicerack.model.Slot

interface LedGroupSource {
    fun get(slot: Slot): AonOutcome<Set<Led>>
    fun add(slot: Slot, led: Led): UnitOutcome
    fun remove(slot: Slot, led: Led) : UnitOutcome
    fun get(): AonOutcome<Map<Slot, Set<Led>>>
}