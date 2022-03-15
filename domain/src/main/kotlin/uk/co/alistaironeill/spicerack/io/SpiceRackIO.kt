package uk.co.alistaironeill.spicerack.io

import uk.co.alistaironeill.spicerack.colour.RGB
import uk.co.alistaironeill.spicerack.slot.Led

class SpiceRackIO(private val serialIO: SerialIO) {

    fun clear() {
        SpiceRackCommand.Direct.Reset
            .serialCommands
            .let(serialIO::send)
    }

    fun set(data: Map<RGB, Set<Led>>) {
        (data.map { (rgb, leds) ->
            SpiceRackCommand.Composite.SetRGBLeds(rgb, leds)
        } + SpiceRackCommand.Direct.Refresh)
            .flatMap(SpiceRackCommand::serialCommands)
            .let(serialIO::send)
    }
}