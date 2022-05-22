package uk.co.alistaironeill.spicerack.controller

import com.ubertob.kondor.json.JSet
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import uk.co.alistaironeill.spicerack.controller.SpiceRackControllerHttpHandler.API_ILLUMINATE
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.http.bodyAsJson
import uk.co.alistaironeill.spicerack.http.handle
import uk.co.alistaironeill.spicerack.json.str
import uk.co.alistaironeill.spicerack.model.Spice

class HttpSpiceRackController(private val handler: HttpHandler): SpiceRackController {

    override fun illuminate(names: Set<Spice.Name>): UnitOutcome =
        Request(Method.POST, API_ILLUMINATE)
            .bodyAsJson(names, JSet(str(Spice.Name)))
            .run(handler)
            .handle()
}