package uk.co.alistaironeill.spicerack.spice

import org.junit.jupiter.api.Test
import uk.co.alistaironeill.spicerack.domain.spice.random
import uk.co.alistaironeill.spicerack.json.expectRoundTrips
import uk.co.alistaironeill.spicerack.spice.SpiceUpdate.*

class JSpiceUpdateTest {
    @Test
    fun `can round trip a rename`() =
        SpiceName.random()
            .let(::Rename)
            .run {
                expectRoundTrips(JSpiceUpdate.JRename)
                expectRoundTrips(JSpiceUpdate)
            }

    @Test
    fun `can round trip an add alias`() =
        SpiceName.random()
            .let(::AddAlias)
            .run {
                expectRoundTrips(JSpiceUpdate.JAddAlias)
                expectRoundTrips(JSpiceUpdate)
            }

    @Test
    fun `can round trip a remove alias`() =
        SpiceName.random()
            .let(::RemoveAlias)
            .run {
                expectRoundTrips(JSpiceUpdate.JRemoveAlias)
                expectRoundTrips(JSpiceUpdate)
            }
}