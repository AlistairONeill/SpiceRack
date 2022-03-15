package uk.co.alistaironeill.spicerack.domain.spice

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.assertions.contains
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.error.AlreadyExists
import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.outcome.expectFailure
import uk.co.alistaironeill.spicerack.outcome.expectSuccess

abstract class SpiceSourceTest {
    abstract val source: SpiceSource

    @Nested
    inner class GetById {
        @Test
        fun `returns spice for id`() {
            val name = SpiceName.random()
            val aliases = List(3) { SpiceName.random() }.toSet()

            val id = source.put(name, aliases)

            source.get(id)
                .expectSuccess()
                .isEqualTo(Spice(id, name, aliases))
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

            source.removeAlias(id, alias).expectSuccess()

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
    inner class Remove {
        @Test
        fun `can remove a spice`() {
            val id = source.put(SpiceName.random())

            source.remove(id).expectSuccess()

            source.get(id)
                .expectFailure()
                .isEqualTo(NotFound(id))
        }

        @Test
        fun `returns not found for an unknown spice id`() {
            val id = SpiceId.mint()

            source.remove(id)
                .expectFailure()
                .isEqualTo(NotFound(id))
        }
    }

    private fun SpiceSource.put(name: SpiceName) : SpiceId = put(name, emptySet())

    private fun SpiceSource.put(name: SpiceName, aliases: Set<SpiceName>) : SpiceId =
        create(name)
            .orThrow()
            .id
            .also { id -> aliases.forEach { addAlias(id, it) } }
}

