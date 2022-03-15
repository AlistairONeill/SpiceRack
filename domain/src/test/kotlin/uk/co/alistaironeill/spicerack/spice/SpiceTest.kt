package uk.co.alistaironeill.spicerack.spice

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.domain.spice.random
import uk.co.alistaironeill.spicerack.error.BadRequest.AlreadyExists
import uk.co.alistaironeill.spicerack.error.NotFound

class SpiceTest {
    @Nested
    inner class Error {
        @Test
        fun `can create member NotFound error`() {
            val spiceId = SpiceId.mint()
            val name = SpiceName.random()

            expectThat(NotFound(name, spiceId)) {
                get { type }.isEqualTo("SpiceName")
                get { id }.isEqualTo(name.value)
                get { parentType }.isEqualTo("Spice")
                get { parentId }.isEqualTo(spiceId.value)
            }
        }

        @Test
        fun `can create AlreadyExists error`() {
            val spiceId = SpiceId.mint()
            val name = SpiceName.random()

            expectThat(AlreadyExists(name, spiceId)) {
                get { type }.isEqualTo("SpiceName")
                get { id }.isEqualTo(name.value)
                get { existingType }.isEqualTo("Spice")
                get { existingId }.isEqualTo(spiceId.value)
            }
        }
    }
}