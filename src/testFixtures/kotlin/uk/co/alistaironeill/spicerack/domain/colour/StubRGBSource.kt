package uk.co.alistaironeill.spicerack.domain.colour

class StubRGBSource: RGBSource {
    private val data = mutableMapOf<Colour, RGB>()

    override fun get(colour: Colour) = data[colour]

    override fun set(colour: Colour, rgb: RGB) {
        data[colour] = rgb
    }
}