package uk.co.alistaironeill.spicerack.domain.colour

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.error.NotFound

class ColourTest {
    @Nested
    inner class Error {
        @Test
        fun `creates the correct NotFound error`() {
                val colour = Colour.values().random()

                expectThat(NotFound(colour)) {
                    get { type }.isEqualTo("Colour")
                    get { id }.isEqualTo(colour.name)
                }
        }
    }
}