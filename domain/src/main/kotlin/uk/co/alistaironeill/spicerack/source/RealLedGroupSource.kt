package uk.co.alistaironeill.spicerack.source

import com.ubertob.kondor.outcome.bind
import com.ubertob.kondor.outcome.failIf
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.Led
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Slot
import uk.co.alistaironeill.spicerack.repository.LedGroupRepository

class RealLedGroupSource(
    private val repository: LedGroupRepository
) : LedGroupSource {
    override fun get(slot: Slot): AonOutcome<Set<Led>> = repository.get(slot)

    override fun get(): AonOutcome<Map<Slot, Set<Led>>> = repository.get()

    override fun add(slot: Slot, led: Led) =
        repository.get(slot)
            .transform { leds -> leds + led }
            .bind { leds -> repository.put(slot, leds) }

    override fun remove(slot: Slot, led: Led): UnitOutcome =
        repository.get(slot)
            .failIf( { !it.contains(led) }) { led.NotFound(slot) }
            .transform { leds -> leds - led }
            .bind { leds -> repository.put(slot, leds) }
}