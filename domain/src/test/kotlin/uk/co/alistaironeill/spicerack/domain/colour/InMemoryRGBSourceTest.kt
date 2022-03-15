package uk.co.alistaironeill.spicerack.domain.colour

class InMemoryRGBSourceTest : RGBSourceTest() {
    override val source = InMemoryRGBSource()
}