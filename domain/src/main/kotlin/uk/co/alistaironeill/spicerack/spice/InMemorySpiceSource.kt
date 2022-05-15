package uk.co.alistaironeill.spicerack.spice

import com.ubertob.kondor.outcome.*
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.BadRequest.AlreadyExists
import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.error.UnitOutcome

class InMemorySpiceSource : SpiceSource {
    private val spices = mutableMapOf<SpiceId, Spice>()

    override fun get(): AonOutcome<Set<Spice>> =
        spices.values
            .toSet()
            .asSuccess()

    override fun get(id: SpiceId): AonOutcome<Spice> =
        spices[id].failIfNull(id::NotFound)

    override fun get(name: SpiceName): AonOutcome<Spice> =
        optionalGet(name)
            .failIfNull(name::NotFound)

    override fun create(name: SpiceName): AonOutcome<Spice> =
        failIfExists(name)
            .transform(::factory)
            .withSuccess(::put)

    override fun addAlias(id: SpiceId, name: SpiceName): UnitOutcome =
        failIfExists(name)
            .bind {
                id.update(withAlias(name))
            }

    override fun removeAlias(id: SpiceId, name: SpiceName): UnitOutcome =
        id.update(withoutAlias(name))

    override fun rename(id: SpiceId, name: SpiceName): UnitOutcome =
        failIfExists(name)
            .bind {
                id.update(withName(name))
            }

    override fun setColour(id: SpiceId, colour: RGB): UnitOutcome =
        id.update(withColour(colour))

    override fun delete(id: SpiceId): UnitOutcome =
        get(id)
            .transform { spices.remove(id) }

    private fun optionalGet(name: SpiceName) =
        spices
            .values
            .singleOrNull(canBeCalled(name))

    private fun failIfExists(name: SpiceName): AonOutcome<SpiceName> =
        optionalGet(name)
            ?.let { spice -> AlreadyExists(name, spice.id) }
            ?.asFailure()
            ?: name.asSuccess()

     private fun SpiceId.update(fn: SpiceMutator): UnitOutcome =
        get(this)
            .bind(fn)
            .put()

    private fun withName(name: SpiceName): SpiceMutator = {
        copy(name = name).asSuccess()
    }

    private fun withAlias(name: SpiceName): SpiceMutator = {
        copy(aliases = aliases + name).asSuccess()
    }

    private fun withoutAlias(name: SpiceName): SpiceMutator = {
        if (aliases.contains(name)) {
            copy(aliases = aliases.minus(name))
                .asSuccess()
        } else {
            NotFound(name, id)
                .asFailure()
        }
    }

    private fun withColour(colour: RGB): SpiceMutator = {
        copy(colour = colour).asSuccess()
    }

    private fun factory(name: SpiceName): Spice = Spice(
        SpiceId.mint(),
        name,
        emptySet(),
        RGB.Default
    )

    private fun canBeCalled(name: SpiceName): (Spice) -> Boolean = { spice ->
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