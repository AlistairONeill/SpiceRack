package uk.co.alistaironeill.spicerack.domain.spice

import uk.co.alistaironeill.spicerack.error.NotFound
import java.util.*

data class SpiceId(val value: String) {
    companion object {
        fun mint() = SpiceId(UUID.randomUUID().toString())
    }
}

operator fun NotFound.Companion.invoke(spiceId: SpiceId) = NotFound("Spice", spiceId.value)
