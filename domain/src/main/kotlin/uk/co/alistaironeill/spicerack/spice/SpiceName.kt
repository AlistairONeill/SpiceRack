package uk.co.alistaironeill.spicerack.spice

import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.tinytype.TTCompanionString
import uk.co.alistaironeill.spicerack.tinytype.TinyType

data class SpiceName(override val value: String) : TinyType<String> {
    companion object : TTCompanionString<SpiceName>(::SpiceName)
}

operator fun NotFound.Companion.invoke(name: SpiceName) = NotFound("SpiceName", name.value)
