package uk.co.alistaironeill.spicerack.model

import com.ubertob.kondor.json.JAny
import com.ubertob.kondor.json.jsonnode.JsonNodeObject
import uk.co.alistaironeill.spicerack.json.num
import uk.co.alistaironeill.spicerack.spice.RGB

object JRGB : JAny<RGB>() {
    private val red by num(RGB::red)
    private val green by num(RGB::green)
    private val blue by num(RGB::blue)

    override fun JsonNodeObject.deserializeOrThrow() = RGB(+red, +green, +blue)
}