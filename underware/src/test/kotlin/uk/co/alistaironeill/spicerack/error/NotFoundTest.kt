package uk.co.alistaironeill.spicerack.error

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class NotFoundTest {
    @Test
    fun `returns the correct error message for simple`() {
        expectThat(NotFound.Simple("Foo", "bar"))
            .get { msg }
            .isEqualTo("Could not find Foo[bar]")
    }

    @Test
    fun `returns the correct error message for member`() {
        expectThat(NotFound.Member("Foo", "foo", "Bar", "bar"))
            .get { msg }
            .isEqualTo("Could not find Foo[foo] on Bar[bar]")
    }
}