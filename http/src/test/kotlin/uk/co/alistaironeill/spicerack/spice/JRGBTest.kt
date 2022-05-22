package uk.co.alistaironeill.spicerack.spice

import org.junit.jupiter.api.Test
import uk.co.alistaironeill.spicerack.json.expectRoundTrips
import uk.co.alistaironeill.spicerack.model.JRGB
import uk.co.alistaironeill.spicerack.model.random

class JRGBTest {
    @Test
    fun `can round trip`() = RGB.random().expectRoundTrips(JRGB)
}