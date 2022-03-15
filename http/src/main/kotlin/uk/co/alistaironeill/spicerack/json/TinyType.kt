@file:Suppress("FunctionName")

package uk.co.alistaironeill.spicerack.json

import com.ubertob.kondor.json.JField
import com.ubertob.kondor.json.JNumRepresentable
import com.ubertob.kondor.json.JStringRepresentable
import uk.co.alistaironeill.spicerack.tinytype.TTCompanion
import uk.co.alistaironeill.spicerack.tinytype.TinyType
import uk.co.alistaironeill.spicerack.tinytype.sniffCompanion
import java.math.BigDecimal

fun <T: TinyType<String>> str(companion: TTCompanion<String, T>): JStringRepresentable<T> =
    object : JStringRepresentable<T>() {
        override val cons: (String) -> T = companion.cons
        override val render: (T) -> String = companion.render
    }

fun <T: TinyType<Int>> num(companion: TTCompanion<Int, T>): JNumRepresentable<T> =
    object : JNumRepresentable<T>() {
        override val cons: (BigDecimal) -> T = { it.toInt().let(companion.cons) }
        override val render: (T) -> BigDecimal = { it.value.toBigDecimal() }
    }


@JvmName("bindTinyTypeString")
inline fun <PT : Any, reified TT: TinyType<String>> str(noinline binder: PT.() -> TT) =
    JField(binder, str(sniffCompanion()))

@JvmName("bindTinyTypeInt")
inline fun <PT : Any, reified TT: TinyType<Int>> num(noinline binder: PT.() -> TT) =
    JField(binder, num(sniffCompanion()))
