package uk.co.alistaironeill.spicerack.error

import androidx.compose.runtime.MutableState
import com.ubertob.kondor.outcome.recover

object AlertSingleton {
    lateinit var error : MutableState<AonError?>
}

fun <T> AonOutcome<T>.orAlert(recover: () -> T) = recover { error ->
    AlertSingleton.error.value = error
    recover()
}