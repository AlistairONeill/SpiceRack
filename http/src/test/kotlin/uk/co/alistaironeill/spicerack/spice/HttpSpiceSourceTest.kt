package uk.co.alistaironeill.spicerack.spice

import uk.co.alistaironeill.spicerack.http.asHttpHandler
import uk.co.alistaironeill.spicerack.spice.SpiceSourceHttpHandler.toContractRoutes

class HttpSpiceSourceTest : SpiceSourceTest() {
    override val source = InMemorySpiceSource()
        .toContractRoutes()
        .asHttpHandler()
        .let(::HttpSpiceSource)
}