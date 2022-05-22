package uk.co.alistaironeill.spicerack.spice

import uk.co.alistaironeill.spicerack.http.asHttpHandler
import uk.co.alistaironeill.spicerack.repository.InMemorySpiceRepository
import uk.co.alistaironeill.spicerack.source.RealSpiceSource
import uk.co.alistaironeill.spicerack.spice.SpiceSourceHttpHandler.toContractRoutes

class HttpSpiceSourceTest : SpiceSourceTest() {
    override val source = InMemorySpiceRepository()
        .let(::RealSpiceSource)
        .toContractRoutes()
        .asHttpHandler()
        .let(::HttpSpiceSource)
}