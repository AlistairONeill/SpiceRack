package uk.co.alistaironeill.spicerack.slot

import org.junit.jupiter.api.Test
import uk.co.alistaironeill.spicerack.json.expectRoundTrips

class JLedTest {
    @Test
    fun `can round trip`() {
        Led.random()
            .expectRoundTrips(JLed)
    }
}