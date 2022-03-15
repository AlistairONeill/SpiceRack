package uk.co.alistaironeill.spicerack.error

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class UnexpectedErrorTest {
    @Test
    fun `has correct error message`() {
        expectThat(UnexpectedError(20, "BOOM"))
            .get { msg }
            .isEqualTo("Error[20]: BOOM")
    }
}