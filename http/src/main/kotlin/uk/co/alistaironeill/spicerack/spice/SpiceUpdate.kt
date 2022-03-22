package uk.co.alistaironeill.spicerack.spice

sealed interface SpiceUpdate {
    data class Rename(val name: SpiceName) : SpiceUpdate
    data class AddAlias(val alias: SpiceName) : SpiceUpdate
    data class RemoveAlias(val alias: SpiceName) : SpiceUpdate
}