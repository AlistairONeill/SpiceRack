package uk.co.alistaironeill.spicerack.spice

import uk.co.alistaironeill.spicerack.error.NotFound

data class SpiceName(val value: String) {
    companion object
}

operator fun NotFound.Companion.invoke(name: SpiceName) = NotFound("SpiceName", name.value)
