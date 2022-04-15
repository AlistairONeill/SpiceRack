package uk.co.alistaironeill.spicerack.io

import uk.co.alistaironeill.spicerack.slot.Led
import uk.co.alistaironeill.spicerack.spice.RGB

class StubSpiceRackIO : SpiceRackIO {
    private var colours : Map<RGB, Set<Led>> = emptyMap()

    override fun set(colours: Map<RGB, Set<Led>>) {
        this.colours = colours
    }

    override fun debug() { }
    override fun reset() { }
}