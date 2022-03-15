package uk.co.alistaironeill.spicerack.domain.spice

import uk.co.alistaironeill.spicerack.error.AlreadyExists
import uk.co.alistaironeill.spicerack.error.NotFound

data class Spice(
    val id: SpiceId,
    val name: SpiceName,
    val aliases: Set<SpiceName>
)

operator fun AlreadyExists.Companion.invoke(name: SpiceName, id: SpiceId) = AlreadyExists("SpiceName", name.value, "Spice", id.value)
operator fun NotFound.Companion.invoke(spiceName: SpiceName, id: SpiceId) = NotFound("SpiceName", spiceName.value, "Spice", id.value)
