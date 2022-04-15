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

@JvmName("bindTinyTypeString")
inline fun <PT : Any, reified TT: TinyType<String>> str(noinline binder: PT.() -> TT) =
    JField(binder, str(sniffCompanion()))

@JvmName("bindTinyTypeInt")
inline fun <PT : Any, reified TT: TinyType<Int>> num(noinline binder: PT.() -> TT) =
    JField(
        binder,
        sniffCompanion<Int, TT>()
            .let { companion ->
                object : JNumRepresentable<TT>() {
                    override val cons: (BigDecimal) -> TT = { it.toInt().let(companion.cons) }
                    override val render: (TT) -> BigDecimal = { it.value.toBigDecimal() }
                }
            }
    )

@JvmName("bindTinyTypeByte")
inline fun <PT : Any, reified TT: TinyType<Byte>> num(noinline binder: PT.() -> TT) =
    JField(
        binder,
        sniffCompanion<Byte, TT>()
            .let { companion ->
                object : JNumRepresentable<TT>() {
                    override val cons: (BigDecimal) -> TT = { it.toInt().toByte().let(companion.cons) }
                    override val render: (TT) -> BigDecimal = { it.value.toInt().toBigDecimal() }
                }
            }
    )
