package uk.co.alistaironeill.spicerack.domain.error

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.domain.colour.Colour

class NotFoundTest {
    @Test
    fun `returns the correct error message`() {
        expectThat(NotFound("Foo", "bar")) {
            get { msg }.isEqualTo("Could not find Foo[bar]")
        }
    }

    @Test
    fun `can create from Colour`() {
        val colour = Colour.values().random()

        expectThat(NotFound(colour)) {
            get { type }.isEqualTo("Colour")
            get { id }.isEqualTo(colour.name)
        }
    }
}