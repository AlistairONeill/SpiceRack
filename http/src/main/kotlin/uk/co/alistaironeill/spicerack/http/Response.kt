package uk.co.alistaironeill.spicerack.http

import com.ubertob.kondor.json.JConverter
import com.ubertob.kondor.outcome.asFailure
import com.ubertob.kondor.outcome.asSuccess
import com.ubertob.kondor.outcome.bindFailure
import com.ubertob.kondor.outcome.recover
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.JAonError
import uk.co.alistaironeill.spicerack.error.UnexpectedError
import uk.co.alistaironeill.spicerack.error.UnitOutcome

fun Response.handle(): UnitOutcome =
    if (status == OK) Unit.asSuccess()
    else handleError()

fun <T: Any> Response.handle(converter: JConverter<T>): AonOutcome<T> =
    bodyString()
        .let(converter::fromJson)
        .bindFailure { handleError() }

private fun <T> Response.handleError(): AonOutcome<T> =
    bodyString()
        .let(JAonError::fromJson)
        .recover { UnexpectedError(status.code, it.msg) }
        .asFailure()