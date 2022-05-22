package uk.co.alistaironeill.spicerack.io

import uk.co.alistaironeill.spicerack.model.Led
import uk.co.alistaironeill.spicerack.spice.RGB

interface SpiceRackIO {
    fun set(colours: Map<RGB, Set<Led>>)
    fun debug()
    fun reset()
}