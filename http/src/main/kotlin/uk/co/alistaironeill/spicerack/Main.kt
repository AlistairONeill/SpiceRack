package uk.co.alistaironeill.spicerack

import org.http4k.contract.ContractRoute
import org.http4k.contract.bind
import org.http4k.contract.contract
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer
import uk.co.alistaironeill.spicerack.spice.InMemorySpiceSource
import uk.co.alistaironeill.spicerack.spice.SpiceSourceHttpHandler.toContractRoutes

fun main() {
    InMemorySpiceSource()
        .toContractRoutes()
        .asHttpHandler()
        .let { loggingFilter.then(it) }
        .asServer(Undertow())
        .start()
}

private val loggingFilter = Filter { handler ->
    { request ->
        println()
        println("${request.method} ${request.uri.path}")

        if (request.bodyString().isNotBlank()) {
            println(request.bodyString())
        }

        handler(request)
            .apply { println(status) }
    }
}

private fun List<ContractRoute>.asHttpHandler() : HttpHandler =
    routes("" bind toContract(this))

private fun toContract(routes: List<ContractRoute>) =
    contract {
        this.routes += routes
    }