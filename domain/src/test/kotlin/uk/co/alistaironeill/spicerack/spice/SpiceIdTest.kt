package uk.co.alistaironeill.spicerack.spice

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Spice

class SpiceIdTest {
    @Nested
    inner class Error {
        @Test
        fun `can create correct NotFound error`() {
            val spiceId = Spice.Id.mint()

            expectThat(spiceId.NotFound()) {
                get { type }.isEqualTo("Spice")
                get { id }.isEqualTo(spiceId.value)
            }
        }
    }
}