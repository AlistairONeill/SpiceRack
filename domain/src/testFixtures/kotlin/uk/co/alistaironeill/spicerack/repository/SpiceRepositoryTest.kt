@file:Suppress("FunctionName")

package uk.co.alistaironeill.spicerack.repository

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.single
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Spice
import uk.co.alistaironeill.spicerack.model.random
import uk.co.alistaironeill.spicerack.model.randoms
import uk.co.alistaironeill.spicerack.outcome.expectFailure
import uk.co.alistaironeill.spicerack.outcome.expectSuccess

abstract class SpiceRepositoryTest {
    abstract val repository: SpiceRepository

    @Nested
    inner class Put {
        @Test
        fun `can create a new spice`() {
            val spice = Spice.random()

            repository.put(spice)
                .expectSuccess()

            repository
                .get()
                .expectSuccess()
                .single()
                .isEqualTo(spice)
        }

        @Test
        fun `overwrites spice with same id`() {
            val old = Spice.random()

            repository.put(old)
                .expectSuccess()

            val new = Spice.random().copy(id = old.id)

            repository.put(new)
                .expectSuccess()

            repository
                .get()
                .expectSuccess()
                .single()
                .isEqualTo(new)

            repository
                .get(old.id)
                .expectSuccess()
                .isEqualTo(new)
        }
    }

    @Nested
    inner class GetAll {
        @Test
        fun `returns empty set if nothing present`() {
            repository
                .get()
                .expectSuccess()
                .isEmpty()
        }

        @Test
        fun `returns single item if only one spice exists`() {
            val spice = Spice.random()

            repository.put(spice)
                .expectSuccess()

            repository
                .get()
                .expectSuccess()
                .single()
                .isEqualTo(spice)
        }

        @Test
        fun `returns all spices`() {
            val spices = Spice.randoms()
                .take(5)
                .onEach { spice ->
                    repository.put(spice)
                        .expectSuccess()
                }.toSet()

            repository
                .get()
                .expectSuccess()
                .isEqualTo(spices)
        }
    }

    @Nested
    inner class GetById {
        @Test
        fun `returns not found if id doesn't exists`() {
            val id = Spice.Id.mint()

            repository
                .get(id)
                .expectFailure()
                .isEqualTo(id.NotFound())
        }

        @Test
        fun `returns spice with the correct Id`() {
            val spice = Spice.random()

            repository.put(spice)
                .expectSuccess()

            repository.put(Spice.random())
                .expectSuccess()

            repository.get(spice.id)
                .expectSuccess()
                .isEqualTo(spice)
        }
    }

    @Nested
    inner class GetByName {
        @Test
        fun `returns empty set if name not present`() {
            val name = Spice.Name.random()

            repository
                .get(name)
                .expectSuccess()
                .isEmpty()
        }

        @Test
        fun `returns all spices which have the requested name`() {
            val name = Spice.Name.random()

            val spices = Spice.randoms()
                .take(3)
                .map { spice -> spice.copy(name = name) }
                .onEach { spice ->
                    repository.put(spice)
                        .expectSuccess()
                }
                .toSet()

            Spice.randoms()
                .take(3)
                .onEach { spice ->
                    repository.put(spice)
                        .expectSuccess()
                }

            repository
                .get(name)
                .expectSuccess()
                .isEqualTo(spices)
        }

        @Test
        fun `returns all spices which have the requested name as an alias`() {
            val name = Spice.Name.random()

            val spices = Spice.randoms()
                .take(3)
                .map { spice -> spice.copy(aliases = spice.aliases + name) }
                .onEach { spice ->
                    repository.put(spice)
                        .expectSuccess()
                }
                .toSet()

            Spice.randoms()
                .take(3)
                .onEach { spice ->
                    repository.put(spice)
                        .expectSuccess()
                }

            repository
                .get(name)
                .expectSuccess()
                .isEqualTo(spices)
        }

        @Test
        fun `returns spices which have either the correct name or the name as an alias`() {
            val name = Spice.Name.random()

            val namedSpices = Spice.randoms()
                .take(3)
                .map { spice -> spice.copy(name = name) }
                .onEach { spice ->
                    repository.put(spice)
                        .expectSuccess()
                }
                .toSet()

            val aliasedSpices = Spice.randoms()
                .take(3)
                .map { spice -> spice.copy(aliases = spice.aliases + name ) }
                .onEach { spice ->
                    repository.put(spice)
                        .expectSuccess()
                }
                .toSet()

            repository
                .get(name)
                .expectSuccess()
                .isEqualTo(namedSpices + aliasedSpices)
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `returns NotFound if id not present`() {
            val id = Spice.Id.mint()

            repository.delete(id)
                .expectFailure()
                .isEqualTo(id.NotFound())
        }

        @Test
        fun `deletes the spice if it is present`() {
            val (id, spice) = Spice.random().run { id to this }

            repository.put(spice)
                .expectSuccess()

            repository.delete(id)
                .expectSuccess()

            repository.get(id)
                .expectFailure()
                .isEqualTo(id.NotFound())
        }
    }
}