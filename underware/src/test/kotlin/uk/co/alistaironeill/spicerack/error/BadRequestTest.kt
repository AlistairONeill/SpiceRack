package uk.co.alistaironeill.spicerack.error

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class BadRequestTest {
    @Nested
    inner class AlreadyExistsTest {
        @Test
        fun `has correct error message`() {
            expectThat(AlreadyExists("Foo", "foo", "Bar", "bar"))
                .get { msg }
                .isEqualTo("Foo[foo] already exists as Bar[bar]")
        }
    }
}