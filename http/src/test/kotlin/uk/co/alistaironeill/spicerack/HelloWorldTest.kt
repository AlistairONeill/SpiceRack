package uk.co.alistaironeill.spicerack

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.strikt.bodyString
import org.http4k.strikt.status
import org.junit.jupiter.api.Test
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.assert.expect

class HelloWorldTest {
    private val handler = createHandler()

    @Test
    fun `returns correct response when prompted with Hello`() {
        Request(GET, "/")
            .body("Hello")
            .run(handler)
            .expect {
                status.isEqualTo(OK)
                bodyString.isEqualTo("World")
            }
    }

    @Test
    fun `returns the correct response when receiving an invalid request`() {
        Request(GET, "/")
            .body("???")
            .run(handler)
            .expect {
                status.isEqualTo(BAD_REQUEST)
            }
    }
}