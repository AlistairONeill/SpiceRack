package uk.co.alistaironeill.spicerack.model

import org.junit.jupiter.api.Test
import uk.co.alistaironeill.spicerack.json.expectRoundTrips

class JSpiceTest {
    @Test
    fun `can round trip a spice`() {
        Spice.random().expectRoundTrips(JSpice)
    }
}