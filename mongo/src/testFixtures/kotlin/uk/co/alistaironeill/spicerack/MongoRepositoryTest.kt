package uk.co.alistaironeill.spicerack

import com.mongodb.client.MongoCollection
import org.junit.jupiter.api.BeforeEach

interface MongoRepositoryTest {
    val collection: MongoCollection<*>

    @BeforeEach
    fun clear() {
        collection.drop()
    }
}