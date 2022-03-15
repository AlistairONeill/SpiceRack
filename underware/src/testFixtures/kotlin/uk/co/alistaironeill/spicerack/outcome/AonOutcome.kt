package uk.co.alistaironeill.spicerack.outcome

import com.ubertob.kondor.outcome.Failure
import com.ubertob.kondor.outcome.Outcome
import com.ubertob.kondor.outcome.OutcomeError
import com.ubertob.kondor.outcome.Success
import strikt.api.expectThat
import strikt.assertions.isA

fun <E: OutcomeError> Outcome<E, *>.expectFailure() =
    expectThat(this)
        .isA<Failure<E>>()
        .get(Failure<E>::error)

fun <T> Outcome<*, T>.expectSuccess() =
    expectThat(this)
        .isA<Success<T>>()
        .get(Success<T>::value)
