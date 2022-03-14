package uk.co.alistaironeill.spicerack.domain.error

import uk.co.alistaironeill.spicerack.domain.spice.SpiceId
import uk.co.alistaironeill.spicerack.domain.spice.SpiceName

sealed interface BadRequest : AonError

//TODO: [AON] Clean up with TT
data class AlreadyExists internal constructor(val type: String, val id: String, val existingType: String, val existingId: String) : BadRequest {
    override val msg: String = "$type[$id] already exists as $existingType[$existingId]"

    companion object {
        operator fun invoke(name: SpiceName, id: SpiceId) = AlreadyExists("SpiceName", name.value, "Spice", id.value)
    }
}