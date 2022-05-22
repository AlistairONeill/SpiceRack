package uk.co.alistaironeill.spicerack.model

import uk.co.alistaironeill.spicerack.error.BadRequest.AlreadyExists
import uk.co.alistaironeill.spicerack.error.NotFound
import uk.co.alistaironeill.spicerack.spice.RGB
import uk.co.alistaironeill.spicerack.tinytype.TTCompanionString
import uk.co.alistaironeill.spicerack.tinytype.TTCompanionUUID
import uk.co.alistaironeill.spicerack.tinytype.TinyType

data class Spice(
    val id: Id,
    val name: Name,
    val aliases: Set<Name>,
    val colour: RGB
) {
    data class Id(override val value: String) : TinyType<String> {
        companion object : TTCompanionUUID<Id>(::Id)
    }

    data class Name(override val value: String) : TinyType<String> {
        companion object : TTCompanionString<Name>(::Name)
    }
}


@Suppress("FunctionName")
fun Spice.Name.NotFound() = NotFound("SpiceName", value)

@Suppress("FunctionName")
fun Spice.Id.NotFound() = NotFound("Spice", value)

@Suppress("FunctionName")
fun Spice.Name.NotFound(id: Spice.Id) = NotFound("SpiceName", value, "Spice", id.value)

@Suppress
fun Spice.Name.AlreadyExists(id: Spice.Id) = AlreadyExists("SpiceName", value, "Spice", id.value)
