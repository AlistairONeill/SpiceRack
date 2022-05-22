package uk.co.alistaironeill.spicerack.source

import uk.co.alistaironeill.spicerack.repository.InMemorySpiceRepository
import uk.co.alistaironeill.spicerack.spice.SpiceSourceTest

class RealSpiceSourceTest: SpiceSourceTest() {
    override val source = RealSpiceSource(InMemorySpiceRepository())
}