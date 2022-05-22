package uk.co.alistaironeill.spicerack.slot

import org.junit.jupiter.api.Test
import uk.co.alistaironeill.spicerack.json.expectRoundTrips
import uk.co.alistaironeill.spicerack.model.JLed
import uk.co.alistaironeill.spicerack.model.Led
import uk.co.alistaironeill.spicerack.model.random

class JLedTest {
    @Test
    fun `can round trip`() {
        Led.random()
            .expectRoundTrips(JLed)
    }
}