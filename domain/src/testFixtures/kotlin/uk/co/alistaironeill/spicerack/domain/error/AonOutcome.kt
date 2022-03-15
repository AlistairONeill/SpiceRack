package uk.co.alistaironeill.spicerack.domain.error

import com.ubertob.kondor.outcome.Failure
import com.ubertob.kondor.outcome.Success
import strikt.api.expectThat
import strikt.assertions.isA

fun <T> AonOutcome<T>.expectFailure() =
    expectThat(this)
        .isA<Failure<AonError>>()
        .get(Failure<AonError>::error)

fun <T> AonOutcome<T>.expectSuccess() =
    expectThat(this)
        .isA<Success<T>>()
        .get(Success<T>::value)
