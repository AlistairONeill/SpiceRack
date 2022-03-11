package uk.co.alistaironeill.spicerack.domain.error

import uk.co.alistaironeill.spicerack.domain.colour.Colour

data class NotFound(val type: String, val id: String): AonError {
    override val msg = "Could not find $type[$id]"

    companion object {
        operator fun invoke(colour: Colour) = NotFound("Colour", colour.name)
    }
}