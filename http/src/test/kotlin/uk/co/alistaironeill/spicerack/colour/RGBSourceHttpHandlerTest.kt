package uk.co.alistaironeill.spicerack.colour

import org.http4k.core.Method.GET
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.junit.jupiter.api.Test
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.colour.RGBSourceHttpHandler.RGB_PATH
import uk.co.alistaironeill.spicerack.colour.RGBSourceHttpHandler.toContractRoutes
import uk.co.alistaironeill.spicerack.domain.colour.random
import uk.co.alistaironeill.spicerack.error.UnexpectedError
import uk.co.alistaironeill.spicerack.http.asHttpHandler
import uk.co.alistaironeill.spicerack.http.bodyAsJson
import uk.co.alistaironeill.spicerack.http.handle
import uk.co.alistaironeill.spicerack.outcome.expectFailure

class RGBSourceHttpHandlerTest {
    private val handler = InMemoryRGBSource().toContractRoutes().asHttpHandler()

    @Test
    fun `does not throw when an invalid colour is provided to put`() {
        Request(PUT, "$RGB_PATH/foo")
            .bodyAsJson(RGB.random(), JRGB)
            .run(handler)
            .handle()
            .expectFailure()
            .isA<UnexpectedError>()
            .get { code }
            .isEqualTo(404)
    }

    @Test
    fun `does not throw when no json is provided to put`() {
        Request(PUT, "$RGB_PATH/GREEN")
            .run(handler)
            .handle()
            .expectFailure()
            .isA<UnexpectedError>()
            .get { code }
            .isEqualTo(BAD_REQUEST.code)
    }

    @Test
    fun `does not throw when an invalid colour is provided to get`() {
        Request(GET, "$RGB_PATH/foo")
            .run(handler)
            .handle()
            .expectFailure()
            .isA<UnexpectedError>()
            .get { code }
            .isEqualTo(404)
    }
}