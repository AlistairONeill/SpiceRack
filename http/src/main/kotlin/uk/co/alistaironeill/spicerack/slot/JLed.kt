package uk.co.alistaironeill.spicerack.slot

import com.ubertob.kondor.json.JAny
import com.ubertob.kondor.json.jsonnode.JsonNodeObject
import com.ubertob.kondor.json.str
import uk.co.alistaironeill.spicerack.json.num

object JLed : JAny<Led>() {
    private val strip by str(Led::strip)
    private val index by num(Led::index)
    override fun JsonNodeObject.deserializeOrThrow() = Led(+strip, +index)
}