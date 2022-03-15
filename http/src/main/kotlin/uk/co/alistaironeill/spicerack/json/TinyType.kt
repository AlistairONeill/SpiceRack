@file:Suppress("FunctionName")

package uk.co.alistaironeill.spicerack.json

import com.ubertob.kondor.json.JField
import com.ubertob.kondor.json.JStringRepresentable
import uk.co.alistaironeill.spicerack.tinytype.TTCompanion
import uk.co.alistaironeill.spicerack.tinytype.TinyType
import uk.co.alistaironeill.spicerack.tinytype.sniffCompanion

fun <T: TinyType<String>> JTinyType(companion: TTCompanion<String, T>): JStringRepresentable<T> =
    object : JStringRepresentable<T>() {
        override val cons: (String) -> T = companion.cons
        override val render: (T) -> String = companion.render
    }


@JvmName("bindTinyTypeString")
inline fun <PT : Any, reified TT: TinyType<String>> str(noinline binder: PT.() -> TT) =
    JField(binder, JTinyType(sniffCompanion()))