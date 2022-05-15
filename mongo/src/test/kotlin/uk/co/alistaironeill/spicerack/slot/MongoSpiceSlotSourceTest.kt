package uk.co.alistaironeill.spicerack.slot

import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import uk.co.alistaironeill.spicerack.MongoSourceTest

class MongoSpiceSlotSourceTest : SpiceSlotSourceTest(), MongoSourceTest {
    private val client = KMongo.createClient()
    private val database = client.getDatabase("Test")
    override val collection = database.getCollection<MongoSpiceSlotMembership>("SpiceSlots")
    override val source = MongoSpiceSlotSource(collection)
}