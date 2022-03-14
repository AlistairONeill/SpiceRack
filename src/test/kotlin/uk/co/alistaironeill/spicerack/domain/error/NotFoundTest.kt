package uk.co.alistaironeill.spicerack.domain.error

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import uk.co.alistaironeill.spicerack.domain.colour.Colour
import uk.co.alistaironeill.spicerack.domain.spice.SpiceId
import uk.co.alistaironeill.spicerack.domain.spice.randomSpiceName

class NotFoundTest {
    @Test
    fun `returns the correct error message for simple`() {
        expectThat(NotFound.Simple("Foo", "bar"))
            .get { msg }
            .isEqualTo("Could not find Foo[bar]")
    }

    @Test
    fun `returns the correct error message for member`() {
        expectThat(NotFound.Member("Foo", "foo", "Bar", "bar"))
            .get { msg }
            .isEqualTo("Could not find Foo[foo] on Bar[bar]")
    }

    @Test
    fun `can create from Colour`() {
        val colour = Colour.values().random()

        expectThat(NotFound(colour)) {
            get { type }.isEqualTo("Colour")
            get { id }.isEqualTo(colour.name)
        }
    }

    @Test
    fun `can create from SpiceId`() {
        val spiceId = SpiceId.mint()

        expectThat(NotFound(spiceId)) {
            get { type }.isEqualTo("Spice")
            get { id }.isEqualTo(spiceId.value)
        }
    }

    @Test
    fun `can create from SpiceName`() {
        val name = randomSpiceName()

        expectThat(NotFound(name)) {
            get { type }.isEqualTo("SpiceName")
            get { id }.isEqualTo(name.value)
        }
    }

    @Test
    fun `can create from SpiceName on SpiceId`() {
        val spiceId = SpiceId.mint()
        val name = randomSpiceName()

        expectThat(NotFound(name, spiceId)) {
            get { type }.isEqualTo("SpiceName")
            get { id }.isEqualTo(name.value)
            get { parentType }.isEqualTo("Spice")
            get { parentId }.isEqualTo(spiceId.value)
        }
    }
}