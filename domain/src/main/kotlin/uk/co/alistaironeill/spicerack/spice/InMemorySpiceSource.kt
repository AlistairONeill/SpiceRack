package uk.co.alistaironeill.spicerack.spice

import com.ubertob.kondor.outcome.*
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.BadRequest.AlreadyExists
import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.AlreadyExists
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Spice

class InMemorySpiceSource : SpiceSource {
    private val spices = mutableMapOf<Spice.Id, Spice>()

    override fun get(): AonOutcome<Set<Spice>> =
        spices.values
            .toSet()
            .asSuccess()

    override fun get(id: Spice.Id): AonOutcome<Spice> =
        spices[id].failIfNull(id::NotFound)

    override fun get(name: Spice.Name): AonOutcome<Spice> =
        optionalGet(name)
            .failIfNull(name::NotFound)

    override fun create(name: Spice.Name): AonOutcome<Spice> =
        failIfExists(name)
            .transform(::factory)
            .withSuccess(::put)

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

    override fun delete(id: Spice.Id): UnitOutcome =
        get(id)
            .transform { spices.remove(id) }

    private fun optionalGet(name: Spice.Name) =
        spices
            .values
            .singleOrNull(canBeCalled(name))

    private fun failIfExists(name: Spice.Name): AonOutcome<Spice.Name> =
        optionalGet(name)
            ?.let { spice -> name.AlreadyExists(spice.id) }
            ?.asFailure()
            ?: name.asSuccess()

     private fun Spice.Id.update(fn: SpiceMutator): UnitOutcome =
        get(this)
            .bind(fn)
            .put()

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

    private fun canBeCalled(name: Spice.Name): (Spice) -> Boolean = { spice ->
        spice.name == name || spice.aliases.contains(name)
    }

    private fun AonOutcome<Spice>.put() : UnitOutcome =
        withSuccess(::put)
            .transform {  }

    private fun put(spice: Spice) {
        spices[spice.id] = spice
    }
}

private typealias SpiceMutator = Spice.() -> AonOutcome<Spice>