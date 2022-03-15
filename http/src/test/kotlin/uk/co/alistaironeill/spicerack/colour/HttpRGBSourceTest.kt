package uk.co.alistaironeill.spicerack.colour

import org.http4k.core.Response
import org.http4k.core.Status.Companion.I_M_A_TEAPOT
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.colour.Colour.GREEN
import uk.co.alistaironeill.spicerack.colour.RGBSourceHttpHandler.toContractRoutes
import uk.co.alistaironeill.spicerack.domain.colour.random
import uk.co.alistaironeill.spicerack.error.UnexpectedError
import uk.co.alistaironeill.spicerack.http.asHttpHandler
import uk.co.alistaironeill.spicerack.outcome.expectFailure

class HttpRGBSourceTest : RGBSourceTest() {

    override val source = InMemoryRGBSource()
        .toContractRoutes()
        .asHttpHandler()
        .let(::HttpRGBSource)

    @Nested
    inner class RoutingErrors {
        private val source = HttpRGBSource { Response(I_M_A_TEAPOT) }

        @Test
        fun `handles get failing`() {
            source.get(GREEN)
                .expectFailure()
                .isA<UnexpectedError>()
                .get { code }
                .isEqualTo(I_M_A_TEAPOT.code)
        }

        @Test
        fun `handles set failing`() {
            source.set(GREEN, RGB.random())
                .expectFailure()
                .isA<UnexpectedError>()
                .get { code }
                .isEqualTo(I_M_A_TEAPOT.code)
        }
    }
}