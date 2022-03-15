package uk.co.alistaironeill.spicerack.json

import com.ubertob.kondor.json.ObjectNodeConverter
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.outcome.expectSuccess

fun <T: Any> T.expectRoundTrips(converter: ObjectNodeConverter<in T>) : Unit =
    let(converter::toJson)
        .let(converter::fromJson)
        .expectSuccess()
        .isEqualTo(this)
        .let { }