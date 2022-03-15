package uk.co.alistaironeill.spicerack.json

import com.ubertob.kondor.json.JField
import com.ubertob.kondor.json.JNumRepresentable
import java.math.BigDecimal

object JByte : JNumRepresentable<Byte>() {
    override val cons: (BigDecimal) -> Byte = BigDecimal::byteValueExact
    override val render: (Byte) -> BigDecimal = { it.toInt().toBigDecimal() }
}

@JvmName("bindByte")
fun <PT : Any> num(binder: PT.() -> Byte) = JField(binder, JByte)