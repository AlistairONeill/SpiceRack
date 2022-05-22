package uk.co.alistaironeill.spicerack.spice

import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import uk.co.alistaironeill.spicerack.MongoSourceTest
import uk.co.alistaironeill.spicerack.model.Spice
import uk.co.alistaironeill.spicerack.source.SpiceSource

class MongoSpiceSourceTest : SpiceSourceTest(), MongoSourceTest {
    private val client = KMongo.createClient()
    private val database = client.getDatabase("Test")
    override val collection = database.getCollection<Spice>("Spice")
    override val source: SpiceSource = MongoSpiceSource(collection)
}