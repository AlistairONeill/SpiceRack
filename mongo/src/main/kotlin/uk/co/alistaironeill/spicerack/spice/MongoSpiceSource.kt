package uk.co.alistaironeill.spicerack.spice

import com.mongodb.client.MongoCollection
import com.ubertob.kondor.outcome.*
import org.bson.conversions.Bson
import org.litote.kmongo.*
import uk.co.alistaironeill.spicerack.MongoSource
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.AlreadyExists
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Spice
import uk.co.alistaironeill.spicerack.source.SpiceSource

class MongoSpiceSource(collection: MongoCollection<Spice>) : SpiceSource, MongoSource<Spice>(collection) {
    override fun get(): AonOutcome<Set<Spice>> =
        tryOrFail {
            find()
                .toSet()
        }

    override fun get(id: Spice.Id): AonOutcome<Spice> =
        tryOrFail {
            find(
                Spice::id eq id
            ).singleOrNull()
        }.failIfNull(id::NotFound)

    override fun get(name: Spice.Name): AonOutcome<Spice> =
        tryOrFail {
            find(
                or(
                    Spice::name eq name,
                    Spice::aliases contains name
                )
            ).singleOrNull()
        }.failIfNull(name::NotFound)

    override fun create(name: Spice.Name): AonOutcome<Spice> =
        failIfExists(name)
            .bind {
                tryOrFail {
                    Spice(
                        Spice.Id.mint(),
                        name,
                        emptySet(),
                        RGB.Default
                    ).also(this::insertOne)
                }
            }

    override fun addAlias(id: Spice.Id, name: Spice.Name): UnitOutcome =
        failIfExists(name)
            .bind {
                update(
                    id,
                    addToSet(Spice::aliases, name)
                )
            }.transform { }

    override fun removeAlias(id: Spice.Id, name: Spice.Name): UnitOutcome =
        get(id)
            .transform(Spice::aliases)
            .bind { aliases ->
                if (aliases.contains(name)) {
                    aliases.minus(name).asSuccess()
                } else {
                    name.NotFound(id).asFailure()
                }
            }.bind { aliases ->
                update(
                    id,
                    Spice::aliases setTo aliases
                )
            }.transform { }

    override fun rename(id: Spice.Id, name: Spice.Name): UnitOutcome =
        failIfExists(name)
            .bind {
                update(
                    id,
                    Spice::name setTo name
                )
            }.transform { }

    override fun setColour(id: Spice.Id, colour: RGB): UnitOutcome =
        update(
            id,
            Spice::colour setTo colour
        )

    override fun delete(id: Spice.Id): UnitOutcome =
        tryOrFail {
            deleteMany(
                Spice::id eq id
            )
        }.failIf({it.deletedCount == 0L}) { id.NotFound() }
            .transform { }

    private fun optionalGet(name: Spice.Name): AonOutcome<Spice.Id?> =
        tryOrFail {
            find(
                or(
                    Spice::name eq name,
                    Spice::aliases contains name
                )
            ).singleOrNull()
                ?.id
        }

    private fun failIfExists(name: Spice.Name): UnitOutcome =
        optionalGet(name)
            .bind { extantId ->
                extantId?.let { name.AlreadyExists(it) }?.asFailure()
                    ?: Unit.asSuccess()
            }

    private fun update(id: Spice.Id, bson: Bson): UnitOutcome =
        tryOrFail {
            updateOne(
                Spice::id eq id,
                bson
            )
        }.failIf({ it.matchedCount == 0L }) { id.NotFound() }
            .transform { }

    private fun update(id: Spice.Id, setTo: SetTo<*>): UnitOutcome =
        tryOrFail {
            updateOne(
                Spice::id eq id,
                setTo
            )
        }.failIf({ it.matchedCount == 0L }) { id.NotFound() }
            .transform { }
}