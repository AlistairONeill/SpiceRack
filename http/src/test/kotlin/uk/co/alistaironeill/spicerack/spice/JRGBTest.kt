package uk.co.alistaironeill.spicerack.spice

import org.junit.jupiter.api.Test
import uk.co.alistaironeill.spicerack.domain.colour.random
import uk.co.alistaironeill.spicerack.json.expectRoundTrips

class JRGBTest {
    @Test
    fun `can round trip`() =
        RGB.random()
            .expectRoundTrips(JRGB)
}