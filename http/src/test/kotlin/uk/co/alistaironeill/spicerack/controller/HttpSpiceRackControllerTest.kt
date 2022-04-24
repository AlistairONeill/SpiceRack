package uk.co.alistaironeill.spicerack.controller

import uk.co.alistaironeill.spicerack.controller.SpiceRackControllerHttpHandler.toContractRoutes
import uk.co.alistaironeill.spicerack.http.asHttpHandler

class HttpSpiceRackControllerTest : SpiceRackControllerTest() {
    override val controller = RealSpiceRackController(
        spiceRackIO,
        spiceSource,
        ledGroupSource,
        slotSource
    )
        .toContractRoutes()
        .asHttpHandler()
        .let(::HttpSpiceRackController)
}