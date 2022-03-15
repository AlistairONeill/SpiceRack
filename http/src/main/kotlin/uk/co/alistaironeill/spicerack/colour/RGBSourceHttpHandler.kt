package uk.co.alistaironeill.spicerack.colour

import org.http4k.contract.ContractRoute
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Method.GET
import org.http4k.core.Method.PUT
import org.http4k.lens.Path
import org.http4k.lens.enum
import uk.co.alistaironeill.spicerack.error.perform
import uk.co.alistaironeill.spicerack.error.toLens
import uk.co.alistaironeill.spicerack.error.toResponse

object RGBSourceHttpHandler {
    const val RGB_PATH = "/api/rgb"

    private val colourPath = Path.enum<Colour>().of("Colour")
    private val rgbLens = JRGB.toLens()

    fun RGBSource.toContractRoutes() =
        listOf(
            toGetHandler(),
            toSetHandler()
        )

    private fun RGBSource.toGetHandler() : ContractRoute =
        RGB_PATH / colourPath meta {

        } bindContract GET to { colour ->
            {
                perform {
                    get(colour)
                }.toResponse(JRGB)
            }
        }

    private fun RGBSource.toSetHandler() : ContractRoute =
        RGB_PATH / colourPath meta {

        } bindContract PUT to { colour ->
            { request ->
                rgbLens(request)
                    .perform { set(colour, this) }
                    .toResponse()
            }
        }
}
