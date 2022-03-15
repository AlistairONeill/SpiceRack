package uk.co.alistaironeill.spicerack.error

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.co.alistaironeill.spicerack.error.BadRequest.AlreadyExists
import uk.co.alistaironeill.spicerack.json.expectRoundTrips

class JAonErrorTest {
    @Nested
    inner class NotFoundError {
        @Test
        fun `can round trip a simple NotFound`() =
            NotFound("Foo", "foo").run {
                expectRoundTrips(JAonError.JNotFound)
                expectRoundTrips(JAonError)
            }

        @Test
        fun `can round trip a member NotFound`() =
            NotFound("Foo", "foo", "Bar", "bar").run {
                expectRoundTrips(JAonError.JMemberNotFound)
                expectRoundTrips(JAonError)
            }
    }

    @Nested
    inner class BadRequestError {
        @Test
        fun `can round trip an AlreadyExists error`() =
            AlreadyExists("Foo", "foo", "Bar", "bar").run {
                expectRoundTrips(JAonError.JAlreadyExists)
                expectRoundTrips(JAonError)
            }

        @Test
        fun `can round trip a generic error`() =
            BadRequest.Generic("Foo").run {
                expectRoundTrips(JAonError.JBadRequest)
                expectRoundTrips(JAonError)
            }
    }

    @Nested
    inner class UnexpectedErrorTest {
        @Test
        fun `can round trip an UnexpectedError`() =
            UnexpectedError(100, "BOOM").run {
                expectRoundTrips(JAonError.JUnexpectedError)
                expectRoundTrips(JAonError)
            }
    }
}