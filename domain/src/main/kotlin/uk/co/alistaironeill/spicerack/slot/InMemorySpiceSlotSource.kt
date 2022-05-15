package uk.co.alistaironeill.spicerack.slot

import com.ubertob.kondor.outcome.asSuccess
import com.ubertob.kondor.outcome.failIfNull
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.spice.NotFound
import uk.co.alistaironeill.spicerack.spice.SpiceId
import uk.co.alistaironeill.spicerack.spice.invoke

class InMemorySpiceSlotSource : SpiceSlotSource {
    private val mappings : MutableSet<Pair<Slot, SpiceId>> = mutableSetOf()

    override fun put(slot: Slot, id: SpiceId): UnitOutcome = Unit.asSuccess().also {
        mappings.removeIf(has(slot))
        mappings.removeIf(has(id))
        mappings.add(slot to id)
    }

    override fun get(id: SpiceId): AonOutcome<Slot> =
        mappings
            .singleOrNull(has(id))
            .failIfNull(id::NotFound)
            .transform(Pair<Slot, SpiceId>::first)

    override fun get(slot: Slot): AonOutcome<SpiceId?> =
        mappings
            .singleOrNull(has(slot))
            ?.second
            .asSuccess()

    private fun has(slot: Slot) : (Pair<Slot, SpiceId>) -> Boolean = { (compare, _) -> compare == slot }
    private fun has(id: SpiceId) : (Pair<Slot, SpiceId>) -> Boolean = { (_, compare) -> compare == id }
}