package uk.co.alistaironeill.spicerack.slot

import kotlin.random.Random.Default.nextBytes
import kotlin.random.Random.Default.nextInt

fun Slot.Companion.random() = Slot(
    Slot.Index.random(),
    Slot.Index.random()
)

fun Slot.Companion.randoms(count: Int) : List<Slot> {
    val randoms = mutableSetOf<Slot>()
    while (randoms.size < count) {
        randoms.add(Slot.random())
    }
    return randoms.toList()
}

fun Slot.Index.Companion.random() = Slot.Index(nextInt())

fun Led.Companion.random() =
    Led(
        Led.Strip.values().random(),
        Led.Index.random()
    )

private fun Byte.Companion.random() : Byte = nextBytes(1)[0]

fun Led.Index.Companion.random() : Led.Index = Led.Index(Byte.random())