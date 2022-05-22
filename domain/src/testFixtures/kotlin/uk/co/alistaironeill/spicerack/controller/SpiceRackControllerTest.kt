@file:Suppress("FunctionName")

package uk.co.alistaironeill.spicerack.controller

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.*
import uk.co.alistaironeill.spicerack.model.random
import uk.co.alistaironeill.spicerack.model.randoms
import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.io.StubSpiceRackIO
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Spice
import uk.co.alistaironeill.spicerack.outcome.expectFailure
import uk.co.alistaironeill.spicerack.outcome.expectSuccess
import uk.co.alistaironeill.spicerack.slot.*
import uk.co.alistaironeill.spicerack.slot.invoke
import uk.co.alistaironeill.spicerack.spice.*

abstract class SpiceRackControllerTest {
    protected val spiceRackIO = StubSpiceRackIO()
    protected val spiceSource = InMemorySpiceSource()
    protected val ledGroupSource = InMemoryLedGroupSource()
    protected val slotSource = InMemorySpiceSlotSource()

    protected abstract val controller : SpiceRackController

    private val rgbs = RGB.randoms()
    private val names = Spice.Name.randoms()
    private val slots = Slot.randoms()
    private val leds = Led.randoms()

    @Nested
    inner class HappyPath {
        @Test
        fun `handles empty set of names`() {
            controller.illuminate(emptySet())
                .expectSuccess()

            expectThat(spiceRackIO)
                .get(StubSpiceRackIO::colours)
                .isNotNull()
                .isEmpty()
        }

        @Test
        fun `handles single name`() {
            val name = names.first()
            val rgb = rgbs.first()
            val id = createSpice(name, rgb)
            val leds = createSlotFor(id)
                .let(::createLedsIn)

            controller.illuminate(setOf(name))
                .expectSuccess()

            expectThat(spiceRackIO.colours)
                .isNotNull()
                .hasSize(1)
                .get { get(rgb) }
                .isNotNull()
                .isEqualTo(leds)
        }

        @Test
        fun `handles multiple names`() {
            val (names, expected) =
                names
                    .take(5)
                    .toSet()
                    .let { names ->
                        names to names.associate { name ->
                            val rgb = rgbs.first()
                            rgb to createSpice(name, rgb)
                                .let(::createSlotFor)
                                .let(::createLedsIn)
                        }
                    }

            controller.illuminate(names)
                .expectSuccess()

            expectThat(spiceRackIO.colours)
                .isEqualTo(expected)
        }

        @Test
        fun `consolidates colour groupings correctly`() {
            val rgb = RGB.random()

            val names = names.take(3).toSet()

            val leds = names.flatMap { name ->
                createSpice(name, rgb)
                    .let(::createSlotFor)
                    .let(::createLedsIn)
            }.toSet()

            controller.illuminate(names)
                .expectSuccess()

            expectThat(spiceRackIO.colours)
                .isNotNull()
                .hasSize(1)
                .get { get(rgb) }
                .isEqualTo(leds)
        }
    }

    @Nested
    inner class SpiceNameNotFound {
        @Test
        fun `returns not found when the spice name is not found`() {
            val name = names.first()

            controller.illuminate(setOf(name))
                .expectFailure()
                .isEqualTo(name.NotFound())
        }

        @Test
        fun `returns not found if any spice name is not found`() {
            val goodNames = names.take(3)
                .toSet()
                .onEach { name ->
                    createSpice(name, rgbs.first())
                        .let(::createSlotFor)
                        .let(::createLedsIn)
                }

            val badName = names.first()

            controller.illuminate(goodNames + badName)
                .expectFailure()
                .isEqualTo(badName.NotFound())
        }
    }

    @Nested
    inner class SlotNotFound {
        @Test
        fun `returns not found when a slot is not found for a spice`() {
            val name = names.first()

            val id = createSpice(name, rgbs.first())

            controller.illuminate(setOf(name))
                .expectFailure()
                .isEqualTo(id.NotFound())

            expectThat(spiceRackIO.colours)
                .isNull()
        }
    }

    @Nested
    inner class LedsNotFound {
        @Test
        fun `returns not found when a slot has no leds assigned`() {
            val name = names.first()

            val slot = createSpice(name, rgbs.first())
                .let(::createSlotFor)

            controller.illuminate(setOf(name))
                .expectFailure()
                .isEqualTo(NotFound(slot))

            expectThat(spiceRackIO.colours)
                .isNull()
        }
    }

    private fun createSpice(name: Spice.Name, rgb: RGB) : Spice.Id =
        spiceSource.create(name)
            .expectSuccess()
            .subject
            .apply {
                spiceSource.setColour(id, rgb)
                    .expectSuccess()
            }
            .id

    private fun createSlotFor(id: Spice.Id): Slot =
        slots.first()
            .also { slot ->
                slotSource.put(slot, id)
                    .expectSuccess()
            }

    private fun createLedsIn(slot: Slot) : Set<Led> =
        leds
            .take(4)
            .onEach { led ->
                ledGroupSource.add(slot, led)
                    .expectSuccess()
            }.toSet()
}