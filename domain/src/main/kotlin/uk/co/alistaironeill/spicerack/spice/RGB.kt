package uk.co.alistaironeill.spicerack.spice

import kotlin.Byte.Companion.MAX_VALUE
import kotlin.Byte.Companion.MIN_VALUE

data class RGB(val red: Byte, val green: Byte, val blue: Byte) {
    companion object {
        val Default = RGB(MIN_VALUE, MAX_VALUE, MIN_VALUE)
    }
}