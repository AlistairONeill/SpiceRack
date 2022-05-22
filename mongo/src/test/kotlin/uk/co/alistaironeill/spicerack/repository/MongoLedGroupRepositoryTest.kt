package uk.co.alistaironeill.spicerack.repository

import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import uk.co.alistaironeill.spicerack.MongoRepositoryTest

class MongoLedGroupRepositoryTest : LedGroupRepositoryTest(), MongoRepositoryTest {
    private val client = KMongo.createClient()
    private val database = client.getDatabase("Test")
    override val collection = database.getCollection<MongoLedGroup>("LedGroups")
    override val repository = MongoLedGroupRepository(collection)
}