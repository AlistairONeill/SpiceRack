package uk.co.alistaironeill.spicerack.spice

import org.junit.jupiter.api.Test
import uk.co.alistaironeill.spicerack.domain.colour.random
import uk.co.alistaironeill.spicerack.domain.spice.random
import uk.co.alistaironeill.spicerack.json.expectRoundTrips
import uk.co.alistaironeill.spicerack.model.Spice

class JSpiceTest {
    @Test
    fun `can round trip a spice`() {
        Spice(
            Spice.Id.mint(),
            Spice.Name.random(),
            setOf(Spice.Name.random(), Spice.Name.random(), Spice.Name.random()),
            RGB.random()
        ).expectRoundTrips(JSpice)
    }
}