package uk.co.alistaironeill.spicerack.spice

import uk.co.alistaironeill.spicerack.model.Spice

sealed interface SpiceUpdate {
    data class Rename(val name: Spice.Name) : SpiceUpdate
    data class AddAlias(val alias: Spice.Name) : SpiceUpdate
    data class RemoveAlias(val alias: Spice.Name) : SpiceUpdate
    data class SetColour(val colour: RGB): SpiceUpdate
}