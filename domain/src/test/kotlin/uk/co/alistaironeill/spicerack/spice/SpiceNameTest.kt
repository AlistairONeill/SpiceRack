package uk.co.alistaironeill.spicerack.spice

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.model.random
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Spice

class SpiceNameTest {
    @Nested
    inner class Error {
        @Test
        fun `can create correct NotFound error`() {
            val name = Spice.Name.random()

            expectThat(name.NotFound()) {
                get { type }.isEqualTo("SpiceName")
                get { id }.isEqualTo(name.value)
            }
        }
    }
}