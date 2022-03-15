package uk.co.alistaironeill.spicerack.spice

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.domain.spice.random
import uk.co.alistaironeill.spicerack.error.NotFound

class SpiceNameTest {
    @Nested
    inner class Error {
        @Test
        fun `can create correct NotFound error`() {
            val name = SpiceName.random()

            expectThat(NotFound(name)) {
                get { type }.isEqualTo("SpiceName")
                get { id }.isEqualTo(name.value)
            }
        }
    }
}