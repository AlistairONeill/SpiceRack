package uk.co.alistaironeill.spicerack.slot

import com.mongodb.client.MongoCollection
import com.ubertob.kondor.outcome.failIf
import org.litote.kmongo.eq
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.Led
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Slot
import uk.co.alistaironeill.spicerack.repository.MongoRepository

class MongoLedGroupRepository(collection: MongoCollection<MongoLedGroupMembership>) : LedGroupSource,
    MongoRepository<MongoLedGroupMembership>(collection) {
    override fun get(slot: Slot): AonOutcome<Set<Led>> =
        tryOrFail {
            find(
                MongoLedGroupMembership::slot eq slot
            ).map(MongoLedGroupMembership::led)
                .toSet()
        }.failIf( { it.isEmpty() }) { slot.NotFound() }


    override fun get(): AonOutcome<Map<Slot, Set<Led>>> =
        tryOrFail {
            find()
                .groupBy { it.slot }
                .mapValues { (_, memberships) ->
                    memberships.map(MongoLedGroupMembership::led).toSet()
                }
        }

    override fun add(slot: Slot, led: Led): UnitOutcome =
        tryOrFail {
            deleteMany(
                MongoLedGroupMembership::led eq led
            )
            insertOne(
                MongoLedGroupMembership(slot, led)
            )
        }

    override fun clear(slot: Slot): UnitOutcome =
        tryOrFail {
            deleteMany(
                MongoLedGroupMembership::slot eq slot
            )
        }
}

data class MongoLedGroupMembership(val slot: Slot, val led: Led)

