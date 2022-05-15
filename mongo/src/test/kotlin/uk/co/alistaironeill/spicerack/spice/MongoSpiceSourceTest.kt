package uk.co.alistaironeill.spicerack.spice

import com.mongodb.client.MongoCollection
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import uk.co.alistaironeill.spicerack.MongoSourceTest
import uk.co.alistaironeill.spicerack.slot.LedGroupSource
import uk.co.alistaironeill.spicerack.slot.MongoLedGroupMembership
import uk.co.alistaironeill.spicerack.slot.MongoLedGroupSource

class MongoSpiceSourceTest : SpiceSourceTest(), MongoSourceTest {
    private val client = KMongo.createClient()
    private val database = client.getDatabase("Test")
    override val collection = database.getCollection<Spice>("Spice")
    override val source: SpiceSource = MongoSpiceSource(collection)
}