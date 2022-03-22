package uk.co.alistaironeill.spicerack.spice

import org.junit.jupiter.api.Test
import uk.co.alistaironeill.spicerack.colour.RGB
import uk.co.alistaironeill.spicerack.domain.colour.random
import uk.co.alistaironeill.spicerack.domain.spice.random
import uk.co.alistaironeill.spicerack.json.expectRoundTrips

class JSpiceTest {
    @Test
    fun `can round trip a spice`() {
        Spice(
            SpiceId.mint(),
            SpiceName.random(),
            setOf(SpiceName.random(), SpiceName.random(), SpiceName.random()),
            RGB.random()
        ).expectRoundTrips(JSpice)
    }
}