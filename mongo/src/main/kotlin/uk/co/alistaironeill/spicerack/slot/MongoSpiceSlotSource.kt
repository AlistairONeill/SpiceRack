package uk.co.alistaironeill.spicerack.slot

import com.mongodb.client.MongoCollection
import com.ubertob.kondor.outcome.failIfNull
import org.litote.kmongo.eq
import uk.co.alistaironeill.spicerack.MongoSource
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.spice.SpiceId
import uk.co.alistaironeill.spicerack.spice.NotFound

class MongoSpiceSlotSource(collection: MongoCollection<MongoSpiceSlotMembership>): SpiceSlotSource, MongoSource<MongoSpiceSlotMembership>(collection) {
    override fun put(slot: Slot, id: SpiceId): UnitOutcome =
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

    override fun get(id: SpiceId): AonOutcome<Slot> =
        tryOrFail {
            find(MongoSpiceSlotMembership::id eq id)
                .singleOrNull()
                ?.slot
        }.failIfNull(id::NotFound)

    override fun get(slot: Slot): AonOutcome<SpiceId?> =
        tryOrFail {
            find(MongoSpiceSlotMembership::slot eq slot)
                .singleOrNull()
                ?.id
        }
}

data class MongoSpiceSlotMembership(
    val slot: Slot,
    val id: SpiceId
)