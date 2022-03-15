package uk.co.alistaironeill.spicerack.http

import org.http4k.lens.Path
import uk.co.alistaironeill.spicerack.tinytype.TTCompanion
import uk.co.alistaironeill.spicerack.tinytype.TinyType

fun <DOMAIN: TinyType<String>> Path.tt(companion: TTCompanion<String, DOMAIN>) =
    map(
        companion.cons,
        companion.render
    )