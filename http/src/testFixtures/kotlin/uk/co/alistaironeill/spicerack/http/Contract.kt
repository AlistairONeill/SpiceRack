package uk.co.alistaironeill.spicerack.http

import org.http4k.contract.ContractRoute
import org.http4k.contract.bind
import org.http4k.contract.contract
import org.http4k.core.HttpHandler
import org.http4k.routing.routes

fun List<ContractRoute>.asHttpHandler() : HttpHandler =
    routes("" bind toContract(this))

private fun toContract(routes: List<ContractRoute>) =
    contract {
        this.routes += routes
    }