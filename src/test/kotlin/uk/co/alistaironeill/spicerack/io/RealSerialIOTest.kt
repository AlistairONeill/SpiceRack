package uk.co.alistaironeill.spicerack.io

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import java.io.IOException
import java.io.PipedInputStream
import java.io.PipedOutputStream
import kotlin.random.Random

class RealSerialIOTest {
    private val outputStream = PipedOutputStream()
    private val inputStream = PipedInputStream(outputStream)

    private val serialIO = RealSerialIO(
        outputStream,
        PipedInputStream()
    ) { outputStream.close() }

    @Test
    fun `correctly writes to the output stream`() {
        val commands = List(3) { randomSerialCommand() }

        serialIO.send(commands)

        commands.forEach { command ->
            expectThat(inputStream.read().toByte())
                .isEqualTo(command.code)

            expectThat(inputStream.read().toByte())
                .isEqualTo(command.param)
        }
    }

    @Test
    fun `closes using supplied lambda`() {
        serialIO.close()

        expectThrows<IOException> {
            serialIO.send(listOf(randomSerialCommand()))
        }
    }
}