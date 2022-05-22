package uk.co.alistaironeill.spicerack.repository

import com.mongodb.client.MongoCollection
import org.litote.kmongo.eq
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.model.Led
import uk.co.alistaironeill.spicerack.model.Slot

class MongoLedGroupRepository(collection: MongoCollection<MongoLedGroup>) : LedGroupRepository,
    MongoRepository<MongoLedGroup>(collection) {
    override fun get(slot: Slot): AonOutcome<Set<Led>> =
        tryOrFail {
            find(
                MongoLedGroup::slot eq slot
            ).singleOrNull()
                ?.leds
                ?: emptySet()
        }

    override fun get(): AonOutcome<Map<Slot, Set<Led>>> =
        tryOrFail {
            find()
                .associate { ledGroup ->
                    ledGroup.slot to ledGroup.leds
                }
        }

    override fun put(slot: Slot, leds: Set<Led>): UnitOutcome =
        tryOrFail {
            deleteMany(
                MongoLedGroup::slot eq slot
            )

            insertOne(
                MongoLedGroup(slot, leds)
            )
        }
}

data class MongoLedGroup(
    val slot: Slot,
    val leds: Set<Led>
)

