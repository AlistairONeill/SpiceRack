package uk.co.alistaironeill.spicerack.io

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.map
import strikt.assertions.single
import uk.co.alistaironeill.spicerack.model.Led
import uk.co.alistaironeill.spicerack.model.Led.Strip.ONE
import uk.co.alistaironeill.spicerack.model.Led.Strip.ZERO
import uk.co.alistaironeill.spicerack.spice.RGB

class RealSpiceRackIOTest {
    private val serial = StubSerialIO()
    private val spiceRack = RealSpiceRackIO(serial)

    @Nested
    inner class Reset {
        @Test
        fun `calling reset sends the correct command`() {
            spiceRack.reset()

            expectThat(serial)
                .get(StubSerialIO::received)
                .single()
                .get(SerialCommand::code)
                .isEqualTo(0)
        }
    }

    @Nested
    inner class Debug {
        @Test
        fun `calling debug sends the correct command`() {
            spiceRack.debug()

            expectThat(serial)
                .get(StubSerialIO::received)
                .single()
                .get(SerialCommand::code)
                .isEqualTo(1)
        }
    }

    @Nested
    inner class Set {
        @Test
        fun `param contents is sandwiched between a reset and a refresh`() {
            spiceRack.set(emptyMap())

            expectThat(serial)
                .get(StubSerialIO::received)
                .map(SerialCommand::code)
                .isEqualTo(listOf(0, 7))
        }

        @Test
        fun `correctly calls the commands to set colours`() {
            spiceRack.set(
                mapOf(
                    RGB(5, 19,23) to setOf(
                        Led(ZERO, Led.Index(17)),
                        Led(ONE, Led.Index(2)),
                        Led(ZERO, Led.Index(4))
                    ),
                    RGB(2, 3, 0) to setOf(
                        Led(ONE, Led.Index(40))
                    )
                )
            )

            expectThat(serial)
                .get(StubSerialIO::received)
                .get(MutableList<SerialCommand>::toList)
                .isEqualTo(
                    listOf(
                        // Reset
                        SerialCommand(0, 0),

                        // Set RGB
                        SerialCommand(4, 5),
                        SerialCommand(5, 19),
                        SerialCommand(6, 23),

                        // Set LEDs
                        SerialCommand(2, 17),
                        SerialCommand(3, 2),
                        SerialCommand(2, 4),

                        // Set RGB
                        SerialCommand(4, 2),
                        SerialCommand(5, 3),
                        SerialCommand(6, 0),

                        // Set LED
                        SerialCommand(3, 40),

                        // Refresh
                        SerialCommand(7, 0)
                    )
                )
        }
    }
}