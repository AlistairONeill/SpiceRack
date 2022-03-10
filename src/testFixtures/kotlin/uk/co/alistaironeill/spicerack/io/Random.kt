package uk.co.alistaironeill.spicerack.io

import kotlin.random.Random

fun randomSerialCommand(): SerialCommand = Random.Default.nextBytes(2)
    .let { (code, param) -> SerialCommand(code, param) }