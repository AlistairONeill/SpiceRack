@file:Suppress("FunctionName")

package uk.co.alistaironeill.spicerack.model

import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.tinytype.TTCompanionByte
import uk.co.alistaironeill.spicerack.tinytype.TinyType

data class Led(
    val strip: Strip,
    val index: Index
) {
    enum class Strip {
        ZERO, ONE
    }

    data class Index(override val value: Byte) : TinyType<Byte> {
        companion object : TTCompanionByte<Index>(Led::Index)
    }

    companion object
}

fun Led.NotFound(slot: Slot) = NotFound.Companion("LED", "${strip.name},${index.value}", "Slot", "${slot.x.value},${slot.y.value}")