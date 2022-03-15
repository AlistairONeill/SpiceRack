package uk.co.alistaironeill.spicerack.error

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.error.BadRequest.AlreadyExists
import uk.co.alistaironeill.spicerack.error.BadRequest.Generic

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

    @Nested
    inner class GenericTest {
        @Test
        fun `has correct error message`() {
            expectThat(Generic("Foo"))
                .get { msg }
                .isEqualTo("Foo")
        }
    }
}