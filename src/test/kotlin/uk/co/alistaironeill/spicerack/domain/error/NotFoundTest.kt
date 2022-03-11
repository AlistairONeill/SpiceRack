package uk.co.alistaironeill.spicerack.domain.error

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class NotFoundTest {
    @Test
    fun `returns the correct error message`() {
        expectThat(NotFound("Foo", "bar")) {
            get { msg }.isEqualTo("Could not find Foo[bar]")
        }
    }
}