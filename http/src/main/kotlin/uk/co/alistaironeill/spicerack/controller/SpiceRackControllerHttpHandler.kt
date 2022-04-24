package uk.co.alistaironeill.spicerack.controller

import com.ubertob.kondor.json.JSet
import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.Method.POST
import uk.co.alistaironeill.spicerack.error.perform
import uk.co.alistaironeill.spicerack.error.toLens
import uk.co.alistaironeill.spicerack.error.toResponse
import uk.co.alistaironeill.spicerack.json.str
import uk.co.alistaironeill.spicerack.spice.SpiceName

object SpiceRackControllerHttpHandler {
    const val API_ILLUMINATE = "/api/illuminate"

    private val illuminateLens = JSet(str(SpiceName)).toLens()

    fun SpiceRackController.toContractRoutes(): List<ContractRoute> =
        listOf(
            illuminate()
        )

    private fun SpiceRackController.illuminate() =
        API_ILLUMINATE meta {

        } bindContract POST to { request ->
            illuminateLens(request)
                .perform { illuminate(this) }
                .toResponse()
        }
}