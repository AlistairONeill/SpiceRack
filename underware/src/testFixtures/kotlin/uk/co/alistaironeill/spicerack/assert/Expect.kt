package uk.co.alistaironeill.spicerack.assert

import strikt.api.Assertion
import strikt.api.expectThat

fun <T: Any> T.expect(block: Assertion.Builder<T>.() -> Unit) = expectThat(this, block)