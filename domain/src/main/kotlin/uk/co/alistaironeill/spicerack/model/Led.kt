package uk.co.alistaironeill.spicerack.model

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