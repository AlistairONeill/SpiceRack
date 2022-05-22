package uk.co.alistaironeill.spicerack.slot

import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import uk.co.alistaironeill.spicerack.MongoRepositoryTest

class MongoLedGroupSourceTest : LedGroupSourceTest(), MongoRepositoryTest {
    private val client = KMongo.createClient()
    private val database = client.getDatabase("Test")
    override val collection = database.getCollection<MongoLedGroupMembership>("LedGroups")
    override val source: LedGroupSource = MongoLedGroupRepository(collection)
}