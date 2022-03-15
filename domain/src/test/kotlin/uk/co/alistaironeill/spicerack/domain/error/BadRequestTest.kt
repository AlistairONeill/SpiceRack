package uk.co.alistaironeill.spicerack.domain.error

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.domain.spice.SpiceId
import uk.co.alistaironeill.spicerack.domain.spice.SpiceName
import uk.co.alistaironeill.spicerack.domain.spice.random

class BadRequestTest {
    @Nested
    inner class AlreadyExistsTest {
        @Test
        fun `has correct error message`() {
            expectThat(AlreadyExists("Foo", "foo", "Bar", "bar"))
                .get { msg }
                .isEqualTo("Foo[foo] already exists as Bar[bar]")
        }

        @Test
        fun `can create for SpiceName and SpiceId`() {
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