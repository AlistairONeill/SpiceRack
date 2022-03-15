package uk.co.alistaironeill.spicerack.colour

import com.ubertob.kondor.outcome.asSuccess
import com.ubertob.kondor.outcome.failIfNull
import uk.co.alistaironeill.spicerack.error.NotFound

class InMemoryRGBSource: RGBSource {
    private val data = mutableMapOf<Colour, RGB>()

    override fun get(colour: Colour) = data[colour].failIfNull { NotFound(colour) }

    override fun set(colour: Colour, rgb: RGB) = Unit.asSuccess().also {
        data[colour] = rgb
    }
}