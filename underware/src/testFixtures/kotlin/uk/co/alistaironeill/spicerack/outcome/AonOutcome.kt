package uk.co.alistaironeill.spicerack.outcome

import com.ubertob.kondor.outcome.Failure
import com.ubertob.kondor.outcome.Success
import strikt.api.expectThat
import strikt.assertions.isA
import uk.co.alistaironeill.spicerack.error.AonError
import uk.co.alistaironeill.spicerack.error.AonOutcome

fun <T> AonOutcome<T>.expectFailure() =
    expectThat(this)
        .isA<Failure<AonError>>()
        .get(Failure<AonError>::error)

fun <T> AonOutcome<T>.expectSuccess() =
    expectThat(this)
        .isA<Success<T>>()
        .get(Success<T>::value)
