package uk.co.alistaironeill.spicerack.colour

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import uk.co.alistaironeill.spicerack.colour.RGBSourceHttpHandler.RGB_PATH
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.http.bodyAsJson
import uk.co.alistaironeill.spicerack.http.handle

class HttpRGBSource(private val handler: HttpHandler): RGBSource {
    override fun get(colour: Colour): AonOutcome<RGB> =
        Request(GET, "$RGB_PATH/${colour.name}")
            .run(handler)
            .handle(JRGB)

    override fun set(colour: Colour, rgb: RGB): UnitOutcome =
        Request(PUT, "$RGB_PATH/${colour.name}")
            .bodyAsJson(rgb, JRGB)
            .run(handler)
            .handle()
}