package uk.co.alistaironeill.spicerack.domain.spice

import uk.co.alistaironeill.spicerack.spice.SpiceName
import java.util.*

fun SpiceName.Companion.random() = SpiceName(UUID.randomUUID().toString())