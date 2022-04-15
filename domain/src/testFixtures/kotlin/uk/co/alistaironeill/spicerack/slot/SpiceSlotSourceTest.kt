@file:Suppress("FunctionName")

package uk.co.alistaironeill.spicerack.slot

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.outcome.expectFailure
import uk.co.alistaironeill.spicerack.outcome.expectSuccess
import uk.co.alistaironeill.spicerack.spice.SpiceId
import uk.co.alistaironeill.spicerack.spice.invoke

abstract class SpiceSlotSourceTest {
    abstract val source: SpiceSlotSource

    @Nested
    inner class Put {
        @Test
        fun `can put an id in a slot`() {
            val id = SpiceId.mint()
            val slot = Slot.random()

            source.put(slot, id)
                .expectSuccess()

            source.get(slot)
                .expectSuccess()
                .isEqualTo(id)

            source.get(id)
                .expectSuccess()
                .isEqualTo(slot)
        }

        @Test
        fun `putting an id in a slot which already has an id overwrites it`() {
            val oldId = SpiceId.mint()
            val newId = SpiceId.mint()
            val slot = Slot.random()

            source.put(slot, oldId)
                .expectSuccess()

            source.put(slot, newId)
                .expectSuccess()

            source.get(slot)
                .expectSuccess()
                .isEqualTo(newId)

            source.get(newId)
                .expectSuccess()
                .isEqualTo(slot)

            source.get(oldId)
                .expectFailure()
                .isEqualTo(NotFound(oldId))
        }

        @Test
        fun `putting an id in a slot where the id already has a slot overwrites it`() {
            val id = SpiceId.mint()
            val (oldSlot, newSlot) = Slot.randoms(2)

            source.put(oldSlot, id)
                .expectSuccess()

            source.put(newSlot, id)
                .expectSuccess()

            source.get(id)
                .expectSuccess()
                .isEqualTo(newSlot)

            source.get(newSlot)
                .expectSuccess()
                .isEqualTo(id)

            source.get(oldSlot)
                .expectSuccess()
                .isNull()
        }
    }


    @Nested
    inner class GetBySlot {
        private val slot = Slot.random()

        @Test
        fun `returns the id if it is present`() {
            val id = SpiceId.mint()

            source.put(slot, id)
                .expectSuccess()

            source.get(slot)
                .expectSuccess()
                .isEqualTo(id)
        }

        @Test
        fun `returns null if it does not have an id`() {
            source.get(slot)
                .expectSuccess()
                .isNull()
        }
    }

    @Nested
    inner class GetById {
        private val id = SpiceId.mint()

        @Test
        fun `returns the slot if it is present`() {
            val slot = Slot.random()

            source.put(slot, id)
                .expectSuccess()

            source.get(id)
                .expectSuccess()
                .isEqualTo(slot)
        }

        @Test
        fun `returns NotFound error if the id does not have a slot`() {
            source.get(id)
                .expectFailure()
                .isEqualTo(NotFound(id))
        }
    }
}