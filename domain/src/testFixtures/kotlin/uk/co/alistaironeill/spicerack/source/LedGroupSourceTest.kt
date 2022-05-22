package uk.co.alistaironeill.spicerack.source

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
        fun `returns empty set for slot with nothing added`() {
            val slot = slots.first()
            source.get(slot)
                .expectSuccess()
                .isEmpty()
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
    }

    @Nested
    inner class Add {
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
        fun `an LED can belong to multiple slots`() {
            val led = leds.first()
            val (slot1, slot2) = slots.take(2)

            source.add(slot1, led)
                .expectSuccess()

            source.add(slot2, led)
                .expectSuccess()

            source.get(slot1)
                .expectSuccess()
                .single()
                .isEqualTo(led)

            source.get(slot2)
                .expectSuccess()
                .single()
                .isEqualTo(led)
        }
    }

    @Nested
    inner class Remove {
        @Test
        fun `can remove an led in a slot`() {
            val led = leds.first()
            val slot = slots.first()

            source.add(slot, led)
                .expectSuccess()

            source.remove(slot, led)
                .expectSuccess()

            source.get(slot)
                .expectSuccess()
                .isEmpty()
        }

        @Test
        fun `returns NotFound error if trying to remove an led from a slot which doesn't have it`() {
            val led = leds.first()
            val slot = slots.first()

            source.remove(slot, led)
                .expectFailure()
                .isEqualTo(led.NotFound(slot))
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