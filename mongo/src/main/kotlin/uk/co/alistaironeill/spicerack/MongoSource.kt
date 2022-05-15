package uk.co.alistaironeill.spicerack

import com.mongodb.client.MongoCollection
import com.ubertob.kondor.outcome.asFailure
import com.ubertob.kondor.outcome.asSuccess
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnexpectedError

abstract class MongoSource<T>(private val collection: MongoCollection<T>) {
    fun <U> tryOrFail(block: MongoCollection<T>.() -> U): AonOutcome<U> =
        try {
            collection.block().asSuccess()
        } catch (e: Exception) {
            UnexpectedError(-1, e.localizedMessage).asFailure()
        }
}