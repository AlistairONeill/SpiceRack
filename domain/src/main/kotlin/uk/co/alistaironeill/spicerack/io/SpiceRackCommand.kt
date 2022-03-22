package uk.co.alistaironeill.spicerack.io

import uk.co.alistaironeill.spicerack.io.SpiceRackCommand.Direct.*
import uk.co.alistaironeill.spicerack.slot.Led
import uk.co.alistaironeill.spicerack.spice.RGB

sealed interface SpiceRackCommand {
    val serialCommands : List<SerialCommand>

    sealed interface Direct : SpiceRackCommand {
        val command: Byte
        val param: Byte
        override val serialCommands get() = listOf(SerialCommand(command, param))

        object Reset : Direct {
            override val command: Byte = 0
            override val param: Byte = 0
        }

        object Debug : Direct {
            override val command: Byte = 1
            override val param: Byte = 0
        }

        data class SetLed(val led: Led) : Direct {
            override val command: Byte = when (led.strip) {
                Led.Strip.ZERO -> 2
                Led.Strip.ONE -> 3
            }

            override val param: Byte = led.index.value.toByte()
        }

        data class SetRed(val value: Byte) : Direct {
            override val command: Byte = 4
            override val param = value
        }

        data class SetGreen(val value: Byte) : Direct {
            override val command: Byte = 5
            override val param = value
        }

        data class SetBlue(val value: Byte) : Direct {
            override val command: Byte = 6
            override val param = value
        }

        object Refresh : Direct {
            override val command: Byte = 7
            override val param: Byte = 0
        }
    }

    sealed interface Composite : SpiceRackCommand {
        val commands: List<SpiceRackCommand>
        override val serialCommands get() = commands.flatMap(SpiceRackCommand::serialCommands)

        data class SetRGB(val rgb: RGB): Composite {
            override val commands = listOf(
                SetRed(rgb.red),
                SetGreen(rgb.green),
                SetBlue(rgb.blue)
            )
        }

        data class SetLeds(val leds: Collection<Led>): Composite {
            override val commands = leds.map(::SetLed)
        }

        data class SetRGBLeds(val rgb: RGB, val leds: Collection<Led>): Composite {
            override val commands = listOf(
                SetRGB(rgb),
                SetLeds(leds)
            )
        }
    }
}
