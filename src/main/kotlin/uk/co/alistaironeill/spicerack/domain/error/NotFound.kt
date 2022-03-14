package uk.co.alistaironeill.spicerack.domain.error

import uk.co.alistaironeill.spicerack.domain.colour.Colour
import uk.co.alistaironeill.spicerack.domain.spice.SpiceId
import uk.co.alistaironeill.spicerack.domain.spice.SpiceName

sealed interface NotFound: AonError {
    data class Simple internal constructor(val type: String, val id: String): NotFound {
        override val msg = "Could not find $type[$id]"
    }

    data class Member internal constructor(val type: String, val id: String, val parentType: String, val parentId: String): NotFound {
        override val msg = "Could not find $type[$id] on $parentType[$parentId]"
    }

    companion object {
        private operator fun invoke(type: String, id: String) = Simple(type, id)
        private operator fun invoke(type: String, id: String, parentType: String, parentId: String) = Member(type, id, parentType, parentId)

        operator fun invoke(colour: Colour) = NotFound("Colour", colour.name)
        operator fun invoke(spiceId: SpiceId) = NotFound("Spice", spiceId.value)
        operator fun invoke(spiceName: SpiceName) = NotFound("SpiceName", spiceName.value)
        operator fun invoke(spiceName: SpiceName, id: SpiceId) = NotFound("SpiceName", spiceName.value, "Spice", id.value)
    }
}
