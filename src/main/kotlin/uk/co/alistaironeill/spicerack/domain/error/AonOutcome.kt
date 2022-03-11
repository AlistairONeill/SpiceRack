package uk.co.alistaironeill.spicerack.domain.error

import com.ubertob.kondor.outcome.Outcome

typealias AonOutcome<T> = Outcome<AonError, T>