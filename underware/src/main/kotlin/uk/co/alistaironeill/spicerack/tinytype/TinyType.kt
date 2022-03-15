package uk.co.alistaironeill.spicerack.tinytype

import java.util.*
import kotlin.reflect.full.companionObjectInstance

interface TinyType<PRIMITIVE> {
    val value: PRIMITIVE
}

interface TTCompanion<PRIMITIVE, DOMAIN: TinyType<PRIMITIVE>> {
    val cons : (PRIMITIVE) -> DOMAIN
    val render: (DOMAIN) -> PRIMITIVE
}

abstract class TTCompanionString<T: TinyType<String>>(override val cons: (String) -> T): TTCompanion<String, T> {
    override val render: (T) -> String = TinyType<String>::value
}

abstract class TTCompanionUUID<T: TinyType<String>>(cons: (String) -> T): TTCompanionString<T>(cons) {
    fun mint() : T = UUID.randomUUID().toString().let(cons)
}

abstract class TTCompanionInt<T: TinyType<Int>>(override val cons: (Int) -> T): TTCompanion<Int, T> {
    override val render: (T) -> Int = TinyType<Int>::value
}

@Suppress("UNCHECKED_CAST")
inline fun <PRIMITIVE: Any, reified DOMAIN: TinyType<PRIMITIVE>> sniffCompanion() : TTCompanion<PRIMITIVE, DOMAIN> =
    DOMAIN::class
        .companionObjectInstance!!
        .let { it as TTCompanion<PRIMITIVE, DOMAIN> }