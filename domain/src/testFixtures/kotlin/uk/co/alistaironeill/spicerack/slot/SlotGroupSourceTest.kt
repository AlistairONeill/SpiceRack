package uk.co.alistaironeill.spicerack.slot

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.single
import uk.co.alistaironeill.spicerack.outcome.expectSuccess

@Suppress("FunctionName")
abstract class LedGroupSourceTest {
    abstract val source: LedGroupSource

    @Nested
    inner class GetBySlot {
        @Test
        fun `returns empty set for unknown slot`() {
            source.get(Slot.random())
                .expectSuccess()
                .isEmpty()
        }

        @Test
        fun `returns leds which have been assigned to the slot`() {
            val slot = Slot.random()
            val leds = List(3) { Led.random() }
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
                source.add(Slot.random(), Led.random())
                    .expectSuccess()
            }

            source.get(Slot.random())
                .expectSuccess()
                .isEmpty()
        }
    }

    @Nested
    inner class Put {
        @Test
        fun `can put an led in a slot`() {
            val led = Led.random()
            val slot = Slot.random()

            source.add(slot, led)
                .expectSuccess()

            source.get(slot)
                .expectSuccess()
                .single()
                .isEqualTo(led)
        }

        @Test
        fun `assigning an led to a slot removes it from another slot`() {
            val led = Led.random()
            val first = Slot.random()
            val second = Slot.random()

            source.add(first, led)
                .expectSuccess()

            source.get(first)
                .expectSuccess()
                .single()
                .isEqualTo(led)

            source.get(second)
                .expectSuccess()
                .isEmpty()

            source.add(second, led)
                .expectSuccess()

            source.get(first)
                .expectSuccess()
                .isEmpty()

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
            val slotToClear = Slot.random()
            val ledsToClear = List(3) { Led.random() }
                .onEach { led ->
                    source.add(slotToClear, led)
                        .expectSuccess()
                }
                .toSet()

            val slotToRemain = Slot.random()
            val ledsToRemain = List(3) { Led.random() }
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
                .expectSuccess()
                .isEmpty()

            source.get(slotToRemain)
                .expectSuccess()
                .isEqualTo(ledsToRemain)
        }

        @Test
        fun `attempting to clear a slot without assigned leds does not return an error`() {
            source.clear(Slot.random())
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
            val expected = List(5) { Slot.random() }
                .associateWith { slot ->
                    List(10) { Led.random() }
                        .onEach { led ->
                            source.add(slot, led)
                                .expectSuccess()
                        }
                        .toSet()
                }

            source.get()
                .expectSuccess()
                .isEqualTo(expected)
        }
    }
}