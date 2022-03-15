package uk.co.alistaironeill.spicerack.spice

import com.ubertob.kondor.json.JAny
import com.ubertob.kondor.json.array
import com.ubertob.kondor.json.jsonnode.JsonNodeObject
import uk.co.alistaironeill.spicerack.json.str

object JSpice : JAny<Spice>() {
    private val id by str(Spice::id)
    private val name by str(Spice::name)
    private val aliases by array(str(SpiceName), Spice::aliases)

    override fun JsonNodeObject.deserializeOrThrow() =
        Spice(
            +id,
            +name,
            +aliases
        )
}