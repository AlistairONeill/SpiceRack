package uk.co.alistaironeill.spicerack.slot

import kotlin.random.Random.Default.nextInt

fun Slot.Companion.random() = Slot(
    Slot.Index.random(),
    Slot.Index.random()
)

fun Slot.Index.Companion.random() = Slot.Index(nextInt())

fun Led.Companion.random() =
    Led(
        Led.Strip.values().random(),
        Led.Index.random()
    )

fun Led.Index.Companion.random() = Led.Index(nextInt())