package uk.co.alistaironeill.spicerack.repository

import com.ubertob.kondor.outcome.asSuccess
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.Led
import uk.co.alistaironeill.spicerack.model.Slot

class InMemoryLedGroupRepository : LedGroupRepository {
    private val data = HashMap<Slot, Set<Led>>()

    override fun get(slot: Slot): AonOutcome<Set<Led>> =
        (data[slot] ?: emptySet()).asSuccess()

    override fun get(): AonOutcome<Map<Slot, Set<Led>>> =
        data.asSuccess()

    override fun put(slot: Slot, leds: Set<Led>): UnitOutcome =
        Unit.asSuccess().also {
            data[slot] = leds
        }
}