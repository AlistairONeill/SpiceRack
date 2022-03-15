package uk.co.alistaironeill.spicerack.json

import com.ubertob.kondor.json.JAny
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.outcome.expectSuccess

fun <T: Any> T.expectRoundTrips(converter: JAny<T>) : Unit =
    let(converter::toJson)
        .let(converter::fromJson)
        .expectSuccess()
        .isEqualTo(this)
        .let { }