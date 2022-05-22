package uk.co.alistaironeill.spicerack.slot

import com.ubertob.kondor.outcome.asSuccess
import com.ubertob.kondor.outcome.failIfNull
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Spice

class InMemorySpiceSlotSource : SpiceSlotSource {
    private val mappings : MutableSet<Pair<Slot, Spice.Id>> = mutableSetOf()

    override fun put(slot: Slot, id: Spice.Id): UnitOutcome = Unit.asSuccess().also {
        mappings.removeIf(has(slot))
        mappings.removeIf(has(id))
        mappings.add(slot to id)
    }

    override fun get(id: Spice.Id): AonOutcome<Slot> =
        mappings
            .singleOrNull(has(id))
            .failIfNull(id::NotFound)
            .transform(Pair<Slot, Spice.Id>::first)

    override fun get(slot: Slot): AonOutcome<Spice.Id?> =
        mappings
            .singleOrNull(has(slot))
            ?.second
            .asSuccess()

    private fun has(slot: Slot) : (Pair<Slot, Spice.Id>) -> Boolean = { (compare, _) -> compare == slot }
    private fun has(id: Spice.Id) : (Pair<Slot, Spice.Id>) -> Boolean = { (_, compare) -> compare == id }
}