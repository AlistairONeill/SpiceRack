package uk.co.alistaironeill.spicerack.slot

import com.ubertob.kondor.outcome.asFailure
import com.ubertob.kondor.outcome.asSuccess
import uk.co.alistaironeill.spicerack.collections.invert
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.error.UnitOutcome

class InMemoryLedGroupSource : LedGroupSource {
    private val data = mutableMapOf<Led, Slot>()

    override fun get(slot: Slot): AonOutcome<Set<Led>> =
        data.filterValues { it == slot }
            .keys
            .let { leds ->
                if (leds.isEmpty()) {
                    NotFound(slot).asFailure()
                } else {
                    leds.asSuccess()
                }
            }

    override fun get(): AonOutcome<Map<Slot, Set<Led>>> =
        data.invert()
            .asSuccess()

    override fun add(slot: Slot, led: Led): UnitOutcome =
        Unit.asSuccess().also {
            data[led] = slot
        }

    override fun clear(slot: Slot): UnitOutcome =
        Unit.asSuccess().also {
            data.invert()[slot]?.forEach(data::remove)
        }
}