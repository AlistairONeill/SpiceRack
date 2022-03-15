package uk.co.alistaironeill.spicerack.slot

import uk.co.alistaironeill.spicerack.tinytype.TTCompanionInt
import uk.co.alistaironeill.spicerack.tinytype.TinyType

data class Led(
    val strip: Strip,
    val index: Index
) {
    enum class Strip {
        ZERO, ONE
    }

    data class Index(override val value: Int) : TinyType<Int> {
        companion object : TTCompanionInt<Index>(::Index)
    }

    companion object
}