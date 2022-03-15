package uk.co.alistaironeill.spicerack.spice

import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.tinytype.TTCompanionUUID
import uk.co.alistaironeill.spicerack.tinytype.TinyType

data class SpiceId(override val value: String) : TinyType<String> {
    companion object : TTCompanionUUID<SpiceId>(::SpiceId)
}

operator fun NotFound.Companion.invoke(spiceId: SpiceId) = NotFound("Spice", spiceId.value)
