package uk.co.alistaironeill.spicerack.error

import com.ubertob.kondor.json.ObjectNodeConverter
import com.ubertob.kondor.outcome.Outcome
import com.ubertob.kondor.outcome.OutcomeError
import com.ubertob.kondor.outcome.recover
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK

sealed interface HttpError : OutcomeError

data class InputError(override val msg: String): HttpError

data class ActionError(val cause: AonError): HttpError {
    override val msg = cause.msg
}

typealias HttpOutcome<T> = Outcome<HttpError, T>

fun HttpOutcome<Unit>.toResponse(): Response =
    transform { Response(OK) }
        .recover(::toResponse)

fun <T: Any> HttpOutcome<T>.toResponse(converter: ObjectNodeConverter<T>): Response =
    transform(converter::toOkResponse)
        .recover(::toResponse)

private fun <T: Any> ObjectNodeConverter<T>.toOkResponse(obj: T): Response =
    createJsonResponse(OK, obj)

private fun <T: Any> ObjectNodeConverter<T>.createJsonResponse(status: Status, obj: T): Response =
    Response(status)
        .body(toJson(obj))
        .header("Content-Type", "application/json")

private fun toResponse(error: HttpError) : Response =
    when (error) {
        is ActionError -> error.cause.toResponse()
        is InputError -> Response(BAD_REQUEST)
    }

private fun AonError.toResponse() : Response = JAonError.createJsonResponse(status, this)

private val AonError.status get() = when (this) {
    is BadRequest -> BAD_REQUEST
    is NotFound -> NOT_FOUND
    is UnexpectedError -> Status(code, message)
}