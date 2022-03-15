package uk.co.alistaironeill.spicerack.domain.spice

data class Spice(
    val id: SpiceId,
    val name: SpiceName,
    val aliases: Set<SpiceName>
)