package uk.co.alistaironeill.spicerack.domain.colour

interface RGBSource {
    fun get(colour: Colour): RGB?
    fun set(colour: Colour, rgb: RGB)
}