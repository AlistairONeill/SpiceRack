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
                    source.put(led, slot)
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
                source.put(Led.random(), Slot.random())
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

            source.put(led, slot)
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

            source.put(led, first)
                .expectSuccess()

            source.get(first)
                .expectSuccess()
                .single()
                .isEqualTo(led)

            source.get(second)
                .expectSuccess()
                .isEmpty()

            source.put(led, second)
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
    inner class Remove {
        @Test
        fun `can remove an led from all slots`() {
            val led = Led.random()
            val slot = Slot.random()

            source.put(led, slot)
                .expectSuccess()

            source.get(slot)
                .expectSuccess()
                .single()
                .isEqualTo(led)

            source.remove(led)
                .expectSuccess()

            source.get(slot)
                .expectSuccess()
                .single()
                .isEqualTo(led)
        }

        @Test
        fun `attempting to remove an unassigned led does not return an error`() {
            source.remove(Led.random())
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
                            source.put(led, slot)
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