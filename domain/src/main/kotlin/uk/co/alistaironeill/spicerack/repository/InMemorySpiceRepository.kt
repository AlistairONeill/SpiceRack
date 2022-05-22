package uk.co.alistaironeill.spicerack.repository

import com.ubertob.kondor.outcome.asFailure
import com.ubertob.kondor.outcome.asSuccess
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Spice

class InMemorySpiceRepository : SpiceRepository {
    private val data = HashMap<Spice.Id, Spice>()

    override fun put(spice: Spice): UnitOutcome =
        Unit.asSuccess()
            .also {
                data[spice.id] = spice
            }

    override fun get(): AonOutcome<Set<Spice>> =
        data.values
            .toSet()
            .asSuccess()

    override fun get(id: Spice.Id): AonOutcome<Spice> =
        data[id]?.asSuccess()
            ?: id.NotFound().asFailure()

    override fun get(name: Spice.Name): AonOutcome<Set<Spice>> =
        data.values
            .filter(canBeCalled(name))
            .toSet()
            .asSuccess()

    override fun delete(id: Spice.Id): UnitOutcome =
        data.remove(id)?.asSuccess()?.transform { }
            ?: id.NotFound().asFailure()

    private fun canBeCalled(name: Spice.Name): (Spice) -> Boolean = { spice ->
        spice.name == name || spice.aliases.contains(name)
    }
}