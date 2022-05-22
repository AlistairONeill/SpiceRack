package uk.co.alistaironeill.spicerack.repository

import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import uk.co.alistaironeill.spicerack.MongoRepositoryTest
import uk.co.alistaironeill.spicerack.model.Spice

class MongoSpiceRepositoryTest : SpiceRepositoryTest(), MongoRepositoryTest {
    private val client = KMongo.createClient()
    private val database = client.getDatabase("Test")
    override val collection = database.getCollection<Spice>("Spice")
    override val repository = MongoSpiceRepository(collection)
}