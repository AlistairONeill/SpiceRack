package uk.co.alistaironeill.spicerack.io

import com.fazecast.jSerialComm.SerialPort
import java.io.InputStream
import java.io.OutputStream


class RealSerialIO(
    private val outputStream: OutputStream,
    private val inputStream: InputStream,
    private val closePort: () -> Unit
) : SerialIO, AutoCloseable {

    companion object {
        fun factory(deviceName: String): RealSerialIO =
            SerialPort.getCommPort(deviceName)
                .apply {
                    setComPortParameters(9600, 8, 1, 0)
                    setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0)
                }.run {
                    openPort()
                    RealSerialIO(
                        outputStream,
                        inputStream
                    ) { closePort() }
                }
    }

    override fun send(commands: List<SerialCommand>) {
        commands.forEach(::send)
        flush()
    }

    private fun send(command: SerialCommand) {
        write(command.code)
        write(command.param)
    }

    private fun flush() {
        outputStream.flush()
    }

    private fun write(byte: Byte) {
        outputStream.write(byte.toInt())
    }

    override fun close() {
        closePort()
    }
}