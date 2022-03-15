package uk.co.alistaironeill.spicerack.colour

import org.junit.jupiter.api.Test
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.colour.Colour.GREEN
import uk.co.alistaironeill.spicerack.domain.colour.random
import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.outcome.expectFailure
import uk.co.alistaironeill.spicerack.outcome.expectSuccess

abstract class RGBSourceTest {
    protected abstract val source: RGBSource

    @Test
    fun `returns NotFound when colour is not present`() {
        source.get(GREEN)
            .expectFailure()
            .isEqualTo(NotFound(GREEN))
    }

    @Test
    fun `can set the RGB value for a colour`() {
        val rgb = RGB.random()
        val colour = Colour.values().random()

        source.set(colour, rgb)

        source.get(colour)
            .expectSuccess()
            .isEqualTo(rgb)
    }
}