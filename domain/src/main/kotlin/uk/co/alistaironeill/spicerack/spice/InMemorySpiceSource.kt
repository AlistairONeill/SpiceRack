package uk.co.alistaironeill.spicerack.spice

import com.ubertob.kondor.outcome.*
import uk.co.alistaironeill.spicerack.colour.RGB
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
        spices[id].failIfNull { NotFound(id) }

    override fun get(name: SpiceName): AonOutcome<Spice> =
        optionalGet(name)
            .failIfNull { NotFound(name) }

    override fun create(name: SpiceName): AonOutcome<Spice> =
        failIfExists(name)
            .transform(::factory)
            .withSuccess(::put)

    override fun addAlias(id: SpiceId, name: SpiceName): UnitOutcome =
        failIfExists(name)
            .bind { get(id) }
            .transform(withAlias(name))
            .withSuccess(::put)
            .transform{}

    override fun removeAlias(id: SpiceId, name: SpiceName): UnitOutcome =
        get(id)
            .bind(withoutAlias(name))
            .withSuccess(::put)
            .transform { }

    override fun rename(id: SpiceId, name: SpiceName): UnitOutcome =
        failIfExists(name)
            .bind { get(id) }
            .transform(withName(name))
            .withSuccess(::put)
            .transform { }

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

    private fun withName(name: SpiceName): Spice.() -> Spice = {
        copy(name = name)
    }

    private fun withAlias(name: SpiceName): Spice.() -> Spice = {
        copy(aliases = aliases + name)
    }

    private fun withoutAlias(name: SpiceName): Spice.() -> AonOutcome<Spice> = {
        if (aliases.contains(name)) {
            copy(aliases = aliases.minus(name))
                .asSuccess()
        } else {
            NotFound(name, id)
                .asFailure()
        }
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

    private fun put(spice: Spice) {
        spices[spice.id] = spice
    }
}