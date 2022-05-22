@file:Suppress("FunctionName")

package uk.co.alistaironeill.spicerack.spice

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.assertions.contains
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.domain.colour.random
import uk.co.alistaironeill.spicerack.domain.spice.random
import uk.co.alistaironeill.spicerack.model.AlreadyExists
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Spice
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
            val expected = List(3) { Spice.Name.random() }
                .map { name ->
                    val aliases = List(3) { Spice.Name.random() }.toSet()
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
            val name = Spice.Name.random()
            val aliases = List(3) { Spice.Name.random() }.toSet()

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
            val id = Spice.Id.mint()

            source.get(id)
                .expectFailure()
                .isEqualTo(id.NotFound())
        }
    }

    @Nested
    inner class GetByName {
        @Test
        fun `can get spice by name`() {
            val name = Spice.Name.random()

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
            val alias = Spice.Name.random()

            val id = source.put(Spice.Name.random(), setOf(Spice.Name.random(), alias, Spice.Name.random()))

            source.get(alias)
                .expectSuccess()
                .and {
                    get { id }.isEqualTo(id)
                    get { aliases }.contains(alias)
                }
        }

        @Test
        fun `returns not found for unknown spiceName`() {
            val name = Spice.Name.random()

            source.get(name)
                .expectFailure()
                .isEqualTo(name.NotFound())
        }
    }

    @Nested
    inner class Create {
        @Test
        fun `can create a spice by name`() {
            val name = Spice.Name.random()

            source.create(name)
                .expectSuccess()
                .get { name }
                .isEqualTo(name)
        }

        @Test
        fun `cannot create a spice with an already used name`() {
            val name = Spice.Name.random()

            val id = source.put(name)

            source.create(name)
                .expectFailure()
                .isEqualTo(name.AlreadyExists(id))
        }

        @Test
        fun `cannot create a spice with an already used name as an alias`() {
            val name = Spice.Name.random()

            val id = source.put(Spice.Name.random(), setOf(name))

            source.create(name)
                .expectFailure()
                .isEqualTo(name.AlreadyExists(id))
        }
    }

    @Nested
    inner class AddAlias {
        @Test
        fun `can add an alias for a spice`() {
            val id = source.put(Spice.Name.random(), emptySet())

            val alias = Spice.Name.random()

            source.addAlias(id, alias).expectSuccess()

            source.get(id)
                .expectSuccess()
                .get { aliases }
                .isEqualTo(setOf(alias))
        }

        @Test
        fun `returns not found for an unknown spiceId`() {
            val spiceId = Spice.Id.mint()

            source.addAlias(spiceId, Spice.Name.random())
                .expectFailure()
                .isEqualTo(spiceId.NotFound())
        }

        @Test
        fun  `returns bad request when name already used`() {
            val name = Spice.Name.random()
            val id = source.put(name, emptySet())

            source.addAlias(id, name)
                .expectFailure()
                .isEqualTo(name.AlreadyExists(id))

            source.addAlias(Spice.Id.mint(), name)
                .expectFailure()
                .isEqualTo(name.AlreadyExists(id))
        }

        @Test
        fun `returns bad request when name already used as an alias`() {
            val name = Spice.Name.random()
            val id = source.put(Spice.Name.random(), setOf(name))

            source.addAlias(Spice.Id.mint(), name)
                .expectFailure()
                .isEqualTo(name.AlreadyExists(id))

            source.addAlias(id, name)
                .expectFailure()
                .isEqualTo(name.AlreadyExists(id))
        }
    }

    @Nested
    inner class RemoveAlias {
        @Test
        fun `can remove alias from a spice with single alias`() {
            val alias = Spice.Name.random()

            val id = source.put(Spice.Name.random(), setOf(alias))

            source.removeAlias(id, alias).expectSuccess()

            source.get(id)
                .expectSuccess()
                .get { aliases }
                .isEmpty()
        }

        @Test
        fun `can remove alias from a spice with multiple aliases`() {
            val alias = Spice.Name.random()

            val otherAliases = List(3) { Spice.Name.random() }.toSet()

            val id = source.put(Spice.Name.random(), otherAliases + alias)

            source.removeAlias(id, alias)
                .expectSuccess()

            source.get(id)
                .expectSuccess()
                .get { aliases }
                .isEqualTo(otherAliases)
        }

        @Test
        fun `returns a not found for unknown SpiceId`() {
            val id = Spice.Id.mint()
            val alias = Spice.Name.random()
            source.removeAlias(id, alias)
                .expectFailure()
                .isEqualTo(id.NotFound())
        }

        @Test
        fun `returns a not found for alias not on spice`() {
            val id = source.put(Spice.Name.random(), setOf(Spice.Name.random()))
            val alias = Spice.Name.random()

            source.removeAlias(id, alias)
                .expectFailure()
                .isEqualTo(alias.NotFound(id))
        }
    }

    @Nested
    inner class Rename {
        @Test
        fun `can rename a spice`() {
            val id = source.put(Spice.Name.random())
            val newName = Spice.Name.random()

            source.rename(id, newName)
                .expectSuccess()

            source.get(id)
                .expectSuccess()
                .get { name }
                .isEqualTo(newName)
        }

        @Test
        fun `returns a NotFound if id does not exist`() {
            val id = Spice.Id.mint()
            source.rename(id, Spice.Name.random())
                .expectFailure()
                .isEqualTo(id.NotFound())
        }

        @Test
        fun  `returns bad request when name already used`() {
            val name = Spice.Name.random()
            val extantId = source.put(name)

            val id = source.put(Spice.Name.random())

            source.rename(id, name)
                .expectFailure()
                .isEqualTo(name.AlreadyExists(extantId))
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `can delete a spice`() {
            val id = source.put(Spice.Name.random())

            source.delete(id).expectSuccess()

            source.get(id)
                .expectFailure()
                .isEqualTo(id.NotFound())
        }

        @Test
        fun `returns not found for an unknown spice id`() {
            val id = Spice.Id.mint()

            source.delete(id)
                .expectFailure()
                .isEqualTo(id.NotFound())
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
            val id = Spice.Id.mint()
            source.setColour(id, RGB.random())
                .expectFailure()
                .isEqualTo(id.NotFound())
        }
    }

    private fun SpiceSource.put(
        name: Spice.Name = Spice.Name.random(),
        aliases: Set<Spice.Name> = emptySet()
    ) : Spice.Id =
        create(name)
            .expectSuccess()
            .subject
            .id
            .also { id -> aliases.forEach { addAlias(id, it) } }
}
