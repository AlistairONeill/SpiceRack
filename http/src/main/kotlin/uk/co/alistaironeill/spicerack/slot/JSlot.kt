package uk.co.alistaironeill.spicerack.slot

import com.ubertob.kondor.json.JAny
import com.ubertob.kondor.json.JStringRepresentable
import com.ubertob.kondor.json.jsonnode.JsonNodeObject
import uk.co.alistaironeill.spicerack.json.num

object JSlot: JAny<Slot>() {
    private val x by num(Slot::x)
    private val y by num(Slot::y)
    override fun JsonNodeObject.deserializeOrThrow() = Slot(+x, +y)
}

object JSlotString: JStringRepresentable<Slot>() {
    override val cons: (String) -> Slot = { JSlot.fromJson(it).orThrow() }
    override val render: (Slot) -> String = JSlot::toJson
}