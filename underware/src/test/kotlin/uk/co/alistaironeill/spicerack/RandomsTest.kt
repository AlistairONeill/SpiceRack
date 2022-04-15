package uk.co.alistaironeill.spicerack

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import kotlin.random.Random

class RandomsTest {

    @Test
    fun `can get distinct random values`() {
        val randoms = distinctRandoms { Random.Default.nextInt(5, 15) }

        val first = randoms.take(3)

        expectThat(first.distinct())
            .isEqualTo(first)

        val second = randoms.take(6)

        expectThat(second.distinct())
            .isEqualTo(second)

        expectThat(first.intersect(second.toSet()))
            .isEmpty()
    }
}