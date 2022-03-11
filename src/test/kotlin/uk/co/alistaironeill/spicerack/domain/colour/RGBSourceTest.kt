package uk.co.alistaironeill.spicerack.domain.colour

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import uk.co.alistaironeill.spicerack.domain.colour.Colour.GREEN

abstract class RGBSourceTest {
    protected abstract val source: RGBSource

    @Test
    fun `returns null when colour is not present`() {
        expectThat(source.get(GREEN)).isNull()
    }

    @Test
    fun `can set the RGB value for a colour`() {
        val rgb = RGB.random()
        val colour = Colour.values().random()

        source.set(colour, rgb)

        expectThat(source.get(colour)).isEqualTo(rgb)
    }
}