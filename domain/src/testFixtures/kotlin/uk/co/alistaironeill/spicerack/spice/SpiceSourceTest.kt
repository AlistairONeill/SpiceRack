@file:Suppress("FunctionName")

package uk.co.alistaironeill.spicerack.spice

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.assertions.contains
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.domain.colour.random
import uk.co.alistaironeill.spicerack.domain.spice.random
import uk.co.alistaironeill.spicerack.error.BadRequest.AlreadyExists
import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.outcome.expectFailure
import uk.co.alistaironeill.spicerack.outcome.expectSuccess

abstract class SpiceSourceTest {
    abstract val source: SpiceSource

    @Nested
    inner class Get {
        @Test
        fun `returns empty set when nothing present`() {
            source.get()
                .expectSuccess()
                .isEmpty()
        }

        @Test
        fun `returns all spices`() {
            val expected = List(3) { SpiceName.random() }
                .map { name ->
                    val aliases = List(3) { SpiceName.random() }.toSet()
                    val id = source.put(name, aliases)
                    Spice(id, name, aliases, RGB.Default)
                }.toSet()

            source.get()
                .expectSuccess()
                .isEqualTo(expected)
        }
    }

    @Nested
    inner class GetById {
        @Test
        fun `returns spice for id`() {
            val name = SpiceName.random()
            val aliases = List(3) { SpiceName.random() }.toSet()

            val id = source.put(name, aliases)

            source.get(id)
                .expectSuccess()
                .and {
                    get { this.id }.isEqualTo(id)
                    get { this.name }.isEqualTo(name)
                    get { this.aliases }.isEqualTo(aliases)
                }
        }

        @Test
        fun `returns not found for unknown spiceId`() {
            val id = SpiceId.mint()

            source.get(id)
                .expectFailure()
                .isEqualTo(NotFound(id))
        }
    }

    @Nested
    inner class GetByName {
        @Test
        fun `can get spice by name`() {
            val name = SpiceName.random()

            val id = source.put(name)

            source.get(name)
                .expectSuccess()
                .and {
                    get { name }.isEqualTo(name)
                    get { id }.isEqualTo(id)
                }
        }

        @Test
        fun `can get spice by alias`() {
            val alias = SpiceName.random()

            val id = source.put(SpiceName.random(), setOf(SpiceName.random(), alias, SpiceName.random()))

            source.get(alias)
                .expectSuccess()
                .and {
                    get { id }.isEqualTo(id)
                    get { aliases }.contains(alias)
                }
        }

        @Test
        fun `returns not found for unknown spiceName`() {
            val name = SpiceName.random()

            source.get(name)
                .expectFailure()
                .isEqualTo(NotFound(name))
        }
    }

    @Nested
    inner class Create {
        @Test
        fun `can create a spice by name`() {
            val name = SpiceName.random()

            source.create(name)
                .expectSuccess()
                .get { name }
                .isEqualTo(name)
        }

        @Test
        fun `cannot create a spice with an already used name`() {
            val name = SpiceName.random()

            val id = source.put(name)

            source.create(name)
                .expectFailure()
                .isEqualTo(AlreadyExists(name, id))
        }

        @Test
        fun `cannot create a spice with an already used name as an alias`() {
            val name = SpiceName.random()

            val id = source.put(SpiceName.random(), setOf(name))

            source.create(name)
                .expectFailure()
                .isEqualTo(AlreadyExists(name, id))
        }
    }

    @Nested
    inner class AddAlias {
        @Test
        fun `can add an alias for a spice`() {
            val id = source.put(SpiceName.random(), emptySet())

            val alias = SpiceName.random()

            source.addAlias(id, alias).expectSuccess()

            source.get(id)
                .expectSuccess()
                .get { aliases }
                .isEqualTo(setOf(alias))
        }

        @Test
        fun `returns not found for an unknown spiceId`() {
            val spiceId = SpiceId.mint()

            source.addAlias(spiceId, SpiceName.random())
                .expectFailure()
                .isEqualTo(NotFound(spiceId))
        }

        @Test
        fun  `returns bad request when name already used`() {
            val name = SpiceName.random()
            val id = source.put(name, emptySet())

            source.addAlias(id, name)
                .expectFailure()
                .isEqualTo(AlreadyExists(name, id))

            source.addAlias(SpiceId.mint(), name)
                .expectFailure()
                .isEqualTo(AlreadyExists(name, id))
        }

        @Test
        fun `returns bad request when name already used as an alias`() {
            val name = SpiceName.random()
            val id = source.put(SpiceName.random(), setOf(name))

            source.addAlias(SpiceId.mint(), name)
                .expectFailure()
                .isEqualTo(AlreadyExists(name, id))

            source.addAlias(id, name)
                .expectFailure()
                .isEqualTo(AlreadyExists(name, id))
        }
    }

    @Nested
    inner class RemoveAlias {
        @Test
        fun `can remove alias from a spice with single alias`() {
            val alias = SpiceName.random()

            val id = source.put(SpiceName.random(), setOf(alias))

            source.removeAlias(id, alias).expectSuccess()

            source.get(id)
                .expectSuccess()
                .get { aliases }
                .isEmpty()
        }

        @Test
        fun `can remove alias from a spice with multiple aliases`() {
            val alias = SpiceName.random()

            val otherAliases = List(3) { SpiceName.random() }.toSet()

            val id = source.put(SpiceName.random(), otherAliases + alias)

            source.removeAlias(id, alias)
                .expectSuccess()

            source.get(id)
                .expectSuccess()
                .get { aliases }
                .isEqualTo(otherAliases)
        }

        @Test
        fun `returns a not found for unknown SpiceId`() {
            val id = SpiceId.mint()
            val alias = SpiceName.random()
            source.removeAlias(id, alias)
                .expectFailure()
                .isEqualTo(NotFound(id))
        }

        @Test
        fun `returns a not found for alias not on spice`() {
            val id = source.put(SpiceName.random(), setOf(SpiceName.random()))
            val alias = SpiceName.random()

            source.removeAlias(id, alias)
                .expectFailure()
                .isEqualTo(NotFound(alias, id))
        }
    }

    @Nested
    inner class Rename {
        @Test
        fun `can rename a spice`() {
            val id = source.put(SpiceName.random())
            val newName = SpiceName.random()

            source.rename(id, newName)
                .expectSuccess()

            source.get(id)
                .expectSuccess()
                .get { name }
                .isEqualTo(newName)
        }

        @Test
        fun `returns a NotFound if id does not exist`() {
            val id = SpiceId.mint()
            source.rename(id, SpiceName.random())
                .expectFailure()
                .isEqualTo(NotFound(id))
        }

        @Test
        fun  `returns bad request when name already used`() {
            val name = SpiceName.random()
            val extantId = source.put(name)

            val id = source.put(SpiceName.random())

            source.rename(id, name)
                .expectFailure()
                .isEqualTo(AlreadyExists(name, extantId))
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `can delete a spice`() {
            val id = source.put(SpiceName.random())

            source.delete(id).expectSuccess()

            source.get(id)
                .expectFailure()
                .isEqualTo(NotFound(id))
        }

        @Test
        fun `returns not found for an unknown spice id`() {
            val id = SpiceId.mint()

            source.delete(id)
                .expectFailure()
                .isEqualTo(NotFound(id))
        }
    }

    @Nested
    inner class SetColour {
        @Test
        fun `can set the colour of a spice`() {
            val id = source.put()
            val newColour = RGB.random()

            source.setColour(id, newColour)
                .expectSuccess()

            source.get(id)
                .expectSuccess()
                .get { this.colour }.isEqualTo(newColour)
        }

        @Test
        fun `returns not found if the spice does not exist`() {
            val id = SpiceId.mint()
            source.setColour(id, RGB.random())
                .expectFailure()
                .isEqualTo(NotFound(id))
        }
    }

    private fun SpiceSource.put(
        name: SpiceName = SpiceName.random(),
        aliases: Set<SpiceName> = emptySet()
    ) : SpiceId =
        create(name)
            .expectSuccess()
            .subject
            .id
            .also { id -> aliases.forEach { addAlias(id, it) } }
}
