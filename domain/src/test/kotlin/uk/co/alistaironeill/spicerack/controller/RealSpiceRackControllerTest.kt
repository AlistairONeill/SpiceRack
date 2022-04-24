package uk.co.alistaironeill.spicerack.controller

class RealSpiceRackControllerTest : SpiceRackControllerTest() {
    override val controller = RealSpiceRackController(
        spiceRackIO,
        spiceSource,
        ledGroupSource,
        slotSource
    )
}