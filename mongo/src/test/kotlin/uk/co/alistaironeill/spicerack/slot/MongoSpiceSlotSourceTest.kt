package uk.co.alistaironeill.spicerack.slot

import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import uk.co.alistaironeill.spicerack.MongoRepositoryTest

class MongoSpiceSlotSourceTest : SpiceSlotSourceTest(), MongoRepositoryTest {
    private val client = KMongo.createClient()
    private val database = client.getDatabase("Test")
    override val collection = database.getCollection<MongoSpiceSlotMembership>("SpiceSlots")
    override val source = MongoSpiceSlotRepository(collection)
}