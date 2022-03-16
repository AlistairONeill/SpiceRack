package uk.co.alistaironeill.spicerack.error

import androidx.compose.runtime.MutableState
import com.ubertob.kondor.outcome.withFailure

object AlertSingleton {
    lateinit var error : MutableState<AonError?>
}

fun <T> AonOutcome<T>.orAlert() = withFailure { AlertSingleton.error.value = it }