package uk.co.alistaironeill.spicerack.io

import java.io.InputStream
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream

class SimulatedArduinoController {
    private val inputStream = PipedInputStream()
    private val outputStream = PipedOutputStream()

    fun createInputStream(): InputStream = PipedInputStream(outputStream)
    fun createOutputStream(): OutputStream = PipedOutputStream(inputStream)
}