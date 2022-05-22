package uk.co.alistaironeill.spicerack.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.result.DeleteResult
import com.ubertob.kondor.outcome.bind
import com.ubertob.kondor.outcome.failIf
import com.ubertob.kondor.outcome.failIfNull
import org.litote.kmongo.contains
import org.litote.kmongo.eq
import org.litote.kmongo.or
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Spice

class MongoSpiceRepository(collection: MongoCollection<Spice>) : SpiceRepository, MongoRepository<Spice>(collection) {
    override fun put(spice: Spice): UnitOutcome =
        remove(spice.id)
            .bind {
                tryOrFail {
                    insertOne(spice)
                }
            }.transform {  }

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

    override fun get(name: Spice.Name): AonOutcome<Set<Spice>> =
        tryOrFail {
            find(
                or(
                    Spice::name eq name,
                    Spice::aliases contains name
                )
            ).toSet()
        }

    override fun delete(id: Spice.Id): UnitOutcome =
        remove(id)
            .failIf({it.deletedCount == 0L}) { id.NotFound() }
            .transform { }

    private fun remove(id: Spice.Id) : AonOutcome<DeleteResult> =
        tryOrFail {
            deleteMany(
                Spice::id eq id
            )
        }
}