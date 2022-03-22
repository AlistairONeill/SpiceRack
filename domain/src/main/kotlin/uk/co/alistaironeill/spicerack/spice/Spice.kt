package uk.co.alistaironeill.spicerack.spice

import uk.co.alistaironeill.spicerack.colour.RGB
import uk.co.alistaironeill.spicerack.error.BadRequest.AlreadyExists
import uk.co.alistaironeill.spicerack.error.NotFound

data class Spice(
    val id: SpiceId,
    val name: SpiceName,
    val aliases: Set<SpiceName>,
    val colour: RGB
)

operator fun AlreadyExists.Companion.invoke(name: SpiceName, id: SpiceId) = AlreadyExists("SpiceName", name.value, "Spice", id.value)
operator fun NotFound.Companion.invoke(spiceName: SpiceName, id: SpiceId) = NotFound("SpiceName", spiceName.value, "Spice", id.value)
