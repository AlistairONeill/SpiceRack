package uk.co.alistaironeill.spicerack.http

import org.http4k.lens.Path
import org.http4k.lens.int
import uk.co.alistaironeill.spicerack.tinytype.TTCompanion
import uk.co.alistaironeill.spicerack.tinytype.TinyType

fun <DOMAIN : TinyType<String>> Path.str(companion: TTCompanion<String, DOMAIN>) =
    map(
        companion.cons,
        companion.render
    )

fun <DOMAIN : TinyType<Int>> Path.int(companion: TTCompanion<Int, DOMAIN>) =
    int().map(
            companion.cons,
            companion.render
        )