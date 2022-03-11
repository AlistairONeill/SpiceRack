package uk.co.alistaironeill.spicerack.domain.colour

import com.ubertob.kondor.outcome.failIfNull
import uk.co.alistaironeill.spicerack.domain.error.NotFound
import uk.co.alistaironeill.spicerack.domain.error.success

class StubRGBSource: RGBSource {
    private val data = mutableMapOf<Colour, RGB>()

    override fun get(colour: Colour) = data[colour].failIfNull { NotFound(colour) }

    override fun set(colour: Colour, rgb: RGB) = success {
        data[colour] = rgb
    }
}