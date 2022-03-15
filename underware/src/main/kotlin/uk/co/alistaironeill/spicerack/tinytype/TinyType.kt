package uk.co.alistaironeill.spicerack.tinytype

import java.util.*

interface TinyType<PRIMITIVE> {
    val value: PRIMITIVE
}

interface TTCompanion<DOMAIN, PRIMITIVE> {
    val cons : (PRIMITIVE) -> DOMAIN
    val render: (DOMAIN) -> PRIMITIVE
}

abstract class TTCompanionString<T: TinyType<String>>(override val cons: (String) -> T): TTCompanion<T, String> {
    override val render: (T) -> String = TinyType<String>::value
}

abstract class TTCompanionUUID<T: TinyType<String>>(cons: (String) -> T): TTCompanionString<T>(cons) {
    fun mint() : T = UUID.randomUUID().toString().let(cons)
}