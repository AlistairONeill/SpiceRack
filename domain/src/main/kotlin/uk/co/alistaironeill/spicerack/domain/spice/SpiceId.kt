package uk.co.alistaironeill.spicerack.domain.spice

import java.util.*

data class SpiceId(val value: String) {
    companion object {
        fun mint() = SpiceId(UUID.randomUUID().toString())
    }
}