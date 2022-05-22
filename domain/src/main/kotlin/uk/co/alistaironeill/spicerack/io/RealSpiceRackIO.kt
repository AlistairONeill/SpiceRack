package uk.co.alistaironeill.spicerack.io

import uk.co.alistaironeill.spicerack.model.Led
import uk.co.alistaironeill.spicerack.model.Led.Strip.ONE
import uk.co.alistaironeill.spicerack.model.Led.Strip.ZERO
import uk.co.alistaironeill.spicerack.spice.RGB

class RealSpiceRackIO(
    private val serial: SerialIO
) : SpiceRackIO {
    @Suppress("FunctionName")
    companion object {
        private fun simpleSerialCommand(code: Byte) = SerialCommand(code, 0)

        private val Reset = simpleSerialCommand(0)

        private val Debug = simpleSerialCommand(1)

        private fun SetLed(led: Led) = when (led.strip) {
            ZERO -> SerialCommand(2, led.index.value)
            ONE -> SerialCommand(3, led.index.value)
        }

        private fun SetRed(value: Byte) = SerialCommand(4, value)
        private fun SetGreen(value: Byte) = SerialCommand(5, value)
        private fun SetBlue(value: Byte) = SerialCommand(6, value)
        private val Refresh = simpleSerialCommand(7)

        private fun commandsFor(leds: Map.Entry<RGB, Set<Led>>): List<SerialCommand> =
            commandsFor(leds.key) + leds.value.map(::SetLed)

        private fun commandsFor(rgb: RGB): List<SerialCommand> = listOf(
            SetRed(rgb.red),
            SetGreen(rgb.green),
            SetBlue(rgb.blue)
        )
    }

    override fun set(colours: Map<RGB, Set<Led>>) = send(
        listOf(Reset) +
                colours.flatMap(::commandsFor) +
                listOf(Refresh)
    )

    override fun debug() = send(Debug)
    override fun reset() = send(Reset)

    private fun send(command: SerialCommand) = send(listOf(command))
    private fun send(commands: List<SerialCommand>) = serial.send(commands)
}
