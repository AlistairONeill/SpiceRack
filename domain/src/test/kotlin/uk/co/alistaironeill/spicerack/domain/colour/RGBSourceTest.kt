package uk.co.alistaironeill.spicerack.domain.colour

import org.junit.jupiter.api.Test
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.domain.colour.Colour.GREEN
import uk.co.alistaironeill.spicerack.domain.error.NotFound
import uk.co.alistaironeill.spicerack.domain.error.expectFailure
import uk.co.alistaironeill.spicerack.domain.error.expectSuccess

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