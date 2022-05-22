@file:Suppress("unused")

package uk.co.alistaironeill.spicerack.slot

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.single
import uk.co.alistaironeill.spicerack.model.Led
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Slot
import uk.co.alistaironeill.spicerack.model.randoms
import uk.co.alistaironeill.spicerack.outcome.expectFailure
import uk.co.alistaironeill.spicerack.outcome.expectSuccess

@Suppress("FunctionName")
abstract class LedGroupSourceTest {
    abstract val source: LedGroupSource

    private val slots = Slot.randoms()
    private val leds = Led.randoms()

    @Nested
    inner class GetBySlot {
        @Test
        fun `returns NotFound for unknown slot`() {
            val slot = slots.first()
            source.get(slot)
                .expectFailure()
                .isEqualTo(slot.NotFound())
        }

        @Test
        fun `returns leds which have been assigned to the slot`() {
            val slot = slots.first()
            val leds = leds
                .take(3)
                .onEach { led ->
                    source.add(slot, led)
                        .expectSuccess()
                }
                .toSet()

            source.get(slot)
                .expectSuccess()
                .isEqualTo(leds)
        }

        @Test
        fun `does not return leds which have been assigned to a different slot`() {
            repeat(3) {
                source.add(slots.first(), leds.first())
                    .expectSuccess()
            }

            val slot = slots.first()

            source.get(slot)
                .expectFailure()
                .isEqualTo(slot.NotFound())
        }
    }

    @Nested
    inner class Put {
        @Test
        fun `can put an led in a slot`() {
            val led = leds.first()
            val slot = slots.first()

            source.add(slot, led)
                .expectSuccess()

            source.get(slot)
                .expectSuccess()
                .single()
                .isEqualTo(led)
        }

        @Test
        fun `assigning an led to a slot removes it from another slot`() {
            val led = leds.first()
            val (first, second) = slots.take(2)

            source.add(first, led)
                .expectSuccess()

            source.get(first)
                .expectSuccess()
                .single()
                .isEqualTo(led)

            source.get(second)
                .expectFailure()
                .isEqualTo(second.NotFound())

            source.add(second, led)
                .expectSuccess()

            source.get(first)
                .expectFailure()
                .isEqualTo(first.NotFound())

            source.get(second)
                .expectSuccess()
                .single()
                .isEqualTo(led)
        }
    }

    @Nested
    inner class Clear {
        @Test
        fun `can clear a slot`() {
            val (slotToClear, slotToRemain) = slots.take(2)
            val ledsToClear = leds
                .take(3)
                .onEach { led ->
                    source.add(slotToClear, led)
                        .expectSuccess()
                }
                .toSet()

            val ledsToRemain = leds
                .take(3)
                .onEach { led ->
                    source.add(slotToRemain, led)
                        .expectSuccess()
                }
                .toSet()

            source.get(slotToClear)
                .expectSuccess()
                .isEqualTo(ledsToClear)

            source.get(slotToRemain)
                .expectSuccess()
                .isEqualTo(ledsToRemain)

            source.clear(slotToClear)
                .expectSuccess()

            source.get(slotToClear)
                .expectFailure()
                .isEqualTo(slotToClear.NotFound())

            source.get(slotToRemain)
                .expectSuccess()
                .isEqualTo(ledsToRemain)
        }

        @Test
        fun `attempting to clear a slot without assigned leds does not return an error`() {
            slots.first()
                .let(source::clear)
                .expectSuccess()
        }
    }

    @Nested
    inner class Get {
        @Test
        fun `can get mapping of all slots to sets of leds when empty`() {
            source.get()
                .expectSuccess()
                .isEmpty()
        }

        @Test
        fun `correctly returns mapping of all slots to sets of leds`() {
            val expected = slots
                .take(5)
                .toSet()
                .associateWith { slot ->
                    leds.take(10)
                        .toSet()
                        .onEach { led ->
                            source.add(slot, led)
                                .expectSuccess()
                        }
                }

            source.get()
                .expectSuccess()
                .isEqualTo(expected)
        }
    }
}