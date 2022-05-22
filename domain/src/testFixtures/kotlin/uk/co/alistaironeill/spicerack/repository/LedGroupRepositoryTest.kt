package uk.co.alistaironeill.spicerack.repository

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.model.Led
import uk.co.alistaironeill.spicerack.model.Slot
import uk.co.alistaironeill.spicerack.model.randoms
import uk.co.alistaironeill.spicerack.outcome.expectSuccess

@Suppress("FunctionName")
abstract class LedGroupRepositoryTest {
    protected abstract val repository : LedGroupRepository

    private val randomLeds = Led.randoms()
    private val randomSlots = Slot.randoms()

    @Nested
    inner class Put {
        @Test
        fun `can put leds in a slot`() {
            val leds = randomLeds.take(3).toSet()
            val slot = randomSlots.first()

            repository.put(slot, leds)
                .expectSuccess()

            repository.get(slot)
                .expectSuccess()
                .isEqualTo(leds)
        }

        @Test
        fun `putting in the same slot overrides what was there before`() {
            val old = randomLeds.take(3).toSet()
            val slot = randomSlots.first()
            val new = randomLeds.take(3).toSet()

            repository.put(slot, old)
                .expectSuccess()

            repository.put(slot, new)
                .expectSuccess()

            repository.get(slot)
                .expectSuccess()
                .isEqualTo(new)
        }
    }

    @Nested
    inner class GetBySlot {
        @Test
        fun `returns empty set if nothing stored for slot`() {
            val slot = randomSlots.first()

            repository.get(slot)
                .expectSuccess()
                .isEmpty()
        }

        @Test
        fun `returns the leds put in a slot`() {
            val (slot1, slot2) = randomSlots.take(2)
            val (leds1, leds2) = List(2) { randomLeds.take(3).toSet() }

            repository.put(slot1, leds1)
                .expectSuccess()

            repository.put(slot2, leds2)
                .expectSuccess()

            repository.get(slot1)
                .expectSuccess()
                .isEqualTo(leds1)

            repository.get(slot2)
                .expectSuccess()
                .isEqualTo(leds2)
        }
    }

    @Nested
    inner class Get {
        @Test
        fun `returns empty map if empty`() {
            repository.get()
                .expectSuccess()
                .isEmpty()
        }

        @Test
        fun `returns map of slots to leds`() {
            val (slot1, slot2) = randomSlots.take(2)
            val (leds1, leds2) = List(2) { randomLeds.take(3).toSet() }

            repository.put(slot1, leds1)
                .expectSuccess()

            repository.put(slot2, leds2)
                .expectSuccess()

            repository.get()
                .expectSuccess()
                .isEqualTo(
                    mapOf(
                        slot1 to leds1,
                        slot2 to leds2
                    )
                )
        }
    }
}