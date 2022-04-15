package uk.co.alistaironeill.spicerack.io

import uk.co.alistaironeill.spicerack.slot.Led
import uk.co.alistaironeill.spicerack.spice.RGB

class StubSpiceRackIO : SpiceRackIO {
    var colours : Map<RGB, Set<Led>>? = null

    override fun set(colours: Map<RGB, Set<Led>>) {
        this.colours = colours
    }

    override fun debug() { }
    override fun reset() { }
}