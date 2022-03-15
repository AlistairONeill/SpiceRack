package uk.co.alistaironeill.spicerack

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK


fun createHandler(): HttpHandler = { request ->
    if (request.bodyString() == "Hello")
        Response(OK).body("World")
    else
        Response(BAD_REQUEST)
}