package uk.co.alistaironeill.spicerack.domain.spice

import java.util.*

fun SpiceName.Companion.random() = SpiceName(UUID.randomUUID().toString())