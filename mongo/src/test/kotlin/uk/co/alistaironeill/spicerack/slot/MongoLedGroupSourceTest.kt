package uk.co.alistaironeill.spicerack.slot

import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import uk.co.alistaironeill.spicerack.MongoSourceTest

class MongoLedGroupSourceTest : LedGroupSourceTest(), MongoSourceTest {
    private val client = KMongo.createClient()
    private val database = client.getDatabase("Test")
    override val collection = database.getCollection<MongoLedGroupMembership>("LedGroups")
    override val source: LedGroupSource = MongoLedGroupSource(collection)
}