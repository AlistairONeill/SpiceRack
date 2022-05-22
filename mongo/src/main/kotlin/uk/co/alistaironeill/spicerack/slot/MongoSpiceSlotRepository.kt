package uk.co.alistaironeill.spicerack.slot

import com.mongodb.client.MongoCollection
import com.ubertob.kondor.outcome.failIfNull
import org.litote.kmongo.eq
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Slot
import uk.co.alistaironeill.spicerack.model.Spice
import uk.co.alistaironeill.spicerack.repository.MongoRepository

class MongoSpiceSlotRepository(collection: MongoCollection<MongoSpiceSlotMembership>): SpiceSlotSource, MongoRepository<MongoSpiceSlotMembership>(collection) {
    override fun put(slot: Slot, id: Spice.Id): UnitOutcome =
        tryOrFail {
            deleteMany(MongoSpiceSlotMembership::id eq id)
            deleteMany(MongoSpiceSlotMembership::slot eq slot)

            insertOne(
                MongoSpiceSlotMembership(
                    slot,
                    id
                )
            )
        }

    override fun get(id: Spice.Id): AonOutcome<Slot> =
        tryOrFail {
            find(MongoSpiceSlotMembership::id eq id)
                .singleOrNull()
                ?.slot
        }.failIfNull(id::NotFound)

    override fun get(slot: Slot): AonOutcome<Spice.Id?> =
        tryOrFail {
            find(MongoSpiceSlotMembership::slot eq slot)
                .singleOrNull()
                ?.id
        }
}

data class MongoSpiceSlotMembership(
    val slot: Slot,
    val id: Spice.Id
)