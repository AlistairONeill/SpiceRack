package uk.co.alistaironeill.spicerack.spice

import com.mongodb.client.MongoCollection
import com.ubertob.kondor.outcome.*
import org.bson.conversions.Bson
import org.litote.kmongo.*
import uk.co.alistaironeill.spicerack.MongoSource
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.BadRequest
import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.error.UnitOutcome

class MongoSpiceSource(collection: MongoCollection<Spice>) : SpiceSource, MongoSource<Spice>(collection) {
    override fun get(): AonOutcome<Set<Spice>> =
        tryOrFail {
            find()
                .toSet()
        }

    override fun get(id: SpiceId): AonOutcome<Spice> =
        tryOrFail {
            find(
                Spice::id eq id
            ).singleOrNull()
        }.failIfNull(id::NotFound)

    override fun get(name: SpiceName): AonOutcome<Spice> =
        tryOrFail {
            find(
                or(
                    Spice::name eq name,
                    Spice::aliases contains name
                )
            ).singleOrNull()
        }.failIfNull(name::NotFound)

    override fun create(name: SpiceName): AonOutcome<Spice> =
        failIfExists(name)
            .bind {
                tryOrFail {
                    Spice(
                        SpiceId.mint(),
                        name,
                        emptySet(),
                        RGB.Default
                    ).also(this::insertOne)
                }
            }

    override fun addAlias(id: SpiceId, name: SpiceName): UnitOutcome =
        failIfExists(name)
            .bind {
                update(
                    id,
                    addToSet(Spice::aliases, name)
                )
            }.transform { }

    override fun removeAlias(id: SpiceId, name: SpiceName): UnitOutcome =
        get(id)
            .transform(Spice::aliases)
            .bind { aliases ->
                if (aliases.contains(name)) {
                    aliases.minus(name).asSuccess()
                } else {
                    NotFound(name, id).asFailure()
                }
            }.bind { aliases ->
                update(
                    id,
                    Spice::aliases setTo aliases
                )
            }.transform { }

    override fun rename(id: SpiceId, name: SpiceName): UnitOutcome =
        failIfExists(name)
            .bind {
                update(
                    id,
                    Spice::name setTo name
                )
            }.transform { }

    override fun setColour(id: SpiceId, colour: RGB): UnitOutcome =
        update(
            id,
            Spice::colour setTo colour
        )

    override fun delete(id: SpiceId): UnitOutcome =
        tryOrFail {
            deleteMany(
                Spice::id eq id
            )
        }.failIf({it.deletedCount == 0L}) { id.NotFound() }
            .transform { }

    private fun optionalGet(name: SpiceName): AonOutcome<SpiceId?> =
        tryOrFail {
            find(
                or(
                    Spice::name eq name,
                    Spice::aliases contains name
                )
            ).singleOrNull()
                ?.id
        }

    private fun failIfExists(name: SpiceName): UnitOutcome =
        optionalGet(name)
            .bind { extantId ->
                extantId?.let { BadRequest.AlreadyExists(name, it) }?.asFailure()
                    ?: Unit.asSuccess()
            }

    private fun update(id: SpiceId, bson: Bson): UnitOutcome =
        tryOrFail {
            updateOne(
                Spice::id eq id,
                bson
            )
        }.failIf({ it.matchedCount == 0L }) { id.NotFound() }
            .transform { }

    private fun update(id: SpiceId, setTo: SetTo<*>): UnitOutcome =
        tryOrFail {
            updateOne(
                Spice::id eq id,
                setTo
            )
        }.failIf({ it.matchedCount == 0L }) { id.NotFound() }
            .transform { }
}