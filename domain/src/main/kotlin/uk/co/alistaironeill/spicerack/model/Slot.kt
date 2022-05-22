package uk.co.alistaironeill.spicerack.model

import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.tinytype.TTCompanionByte
import uk.co.alistaironeill.spicerack.tinytype.TinyType

data class Slot(val x: Index, val y: Index) {
    data class Index(override val value: Byte): TinyType<Byte> {
        companion object : TTCompanionByte<Index>(::Index)
    }
    companion object
}

fun Slot.NotFound() = NotFound("Slot", "${x.value},${y.value}")