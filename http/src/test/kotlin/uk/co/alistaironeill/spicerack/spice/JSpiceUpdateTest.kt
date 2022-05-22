package uk.co.alistaironeill.spicerack.spice

import org.junit.jupiter.api.Test
import uk.co.alistaironeill.spicerack.domain.colour.random
import uk.co.alistaironeill.spicerack.domain.spice.random
import uk.co.alistaironeill.spicerack.json.expectRoundTrips
import uk.co.alistaironeill.spicerack.model.Spice
import uk.co.alistaironeill.spicerack.spice.SpiceUpdate.*

class JSpiceUpdateTest {
    @Test
    fun `can round trip a rename`() =
        Spice.Name.random()
            .let(::Rename)
            .run {
                expectRoundTrips(JSpiceUpdate.JRename)
                expectRoundTrips(JSpiceUpdate)
            }

    @Test
    fun `can round trip an add alias`() =
        Spice.Name.random()
            .let(::AddAlias)
            .run {
                expectRoundTrips(JSpiceUpdate.JAddAlias)
                expectRoundTrips(JSpiceUpdate)
            }

    @Test
    fun `can round trip a remove alias`() =
        Spice.Name.random()
            .let(::RemoveAlias)
            .run {
                expectRoundTrips(JSpiceUpdate.JRemoveAlias)
                expectRoundTrips(JSpiceUpdate)
            }

    @Test
    fun `can round trip a set colour`() =
        RGB.random()
            .let(::SetColour)
            .run {
                expectRoundTrips(JSpiceUpdate.JSetColour)
                expectRoundTrips(JSpiceUpdate)
            }
}