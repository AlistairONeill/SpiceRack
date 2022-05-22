package uk.co.alistaironeill.spicerack.source

import com.ubertob.kondor.outcome.*
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.AlreadyExists
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Spice
import uk.co.alistaironeill.spicerack.repository.SpiceRepository
import uk.co.alistaironeill.spicerack.spice.RGB

class RealSpiceSource(val repository: SpiceRepository) : SpiceSource {

    override fun get(): AonOutcome<Set<Spice>> = repository.get()

    override fun get(id: Spice.Id): AonOutcome<Spice> = repository.get(id)

    override fun get(name: Spice.Name): AonOutcome<Spice> = repository.get(name)
        .transform(Set<Spice>::singleOrNull)
        .failIfNull(name::NotFound)

    override fun create(name: Spice.Name): AonOutcome<Spice> =
        failIfExists(name)
            .transform { factory(name) }
            .bindAlso(repository::put)

    override fun addAlias(id: Spice.Id, name: Spice.Name): UnitOutcome =
        failIfExists(name)
            .bind {
                id.update(withAlias(name))
            }

    override fun removeAlias(id: Spice.Id, name: Spice.Name): UnitOutcome =
        id.update(withoutAlias(name))

    override fun rename(id: Spice.Id, name: Spice.Name): UnitOutcome =
        failIfExists(name)
            .bind {
                id.update(withName(name))
            }

    override fun setColour(id: Spice.Id, colour: RGB): UnitOutcome =
        id.update(withColour(colour))

    override fun delete(id: Spice.Id): UnitOutcome = repository.delete(id)

    private fun failIfExists(name: Spice.Name): UnitOutcome =
        repository.get(name)
            .failIf(Set<Spice>::isNotEmpty) { spices -> name.AlreadyExists(spices.first().id) }
            .transform {  }

     private fun Spice.Id.update(fn: SpiceMutator): UnitOutcome =
        get(this)
            .bind(fn)
            .bind(repository::put)

    private fun withName(name: Spice.Name): SpiceMutator = {
        copy(name = name).asSuccess()
    }

    private fun withAlias(name: Spice.Name): SpiceMutator = {
        copy(aliases = aliases + name).asSuccess()
    }

    private fun withoutAlias(name: Spice.Name): SpiceMutator = {
        if (aliases.contains(name)) {
            copy(aliases = aliases.minus(name))
                .asSuccess()
        } else {
            name.NotFound(id)
                .asFailure()
        }
    }

    private fun withColour(colour: RGB): SpiceMutator = {
        copy(colour = colour).asSuccess()
    }

    private fun factory(name: Spice.Name): Spice = Spice(
        Spice.Id.mint(),
        name,
        emptySet(),
        RGB.Default
    )
}

private typealias SpiceMutator = Spice.() -> AonOutcome<Spice>