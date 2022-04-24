package uk.co.alistaironeill.spicerack.http

import com.ubertob.kondor.json.JsonConverter
import com.ubertob.kondor.json.ObjectNodeConverter
import org.http4k.core.Request

fun <T: Any> Request.bodyAsJson(obj: T, converter: JsonConverter<T, *>): Request =
    body(converter.toJson(obj))
        .header("Content-Type", "application/json")
