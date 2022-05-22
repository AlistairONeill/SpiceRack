package uk.co.alistaironeill.spicerack.controller

import com.ubertob.kondor.outcome.*
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.io.SpiceRackIO
import uk.co.alistaironeill.spicerack.model.Led
import uk.co.alistaironeill.spicerack.model.NotFound
import uk.co.alistaironeill.spicerack.model.Spice
import uk.co.alistaironeill.spicerack.slot.*
import uk.co.alistaironeill.spicerack.source.LedGroupSource
import uk.co.alistaironeill.spicerack.source.SpiceSource
import uk.co.alistaironeill.spicerack.spice.RGB

class RealSpiceRackController(
    private val spiceRackIO: SpiceRackIO,
    private val spiceSource: SpiceSource,
    private val ledGroupSource: LedGroupSource,
    private val spiceSlotSource: SpiceSlotSource
) : SpiceRackController {
    override fun illuminate(names: Set<Spice.Name>): UnitOutcome =
        names.map(spiceSource::get)
            .extractList()
            .flatMap(::withLeds)
            .transform(::toMap)
            .withSuccess(spiceRackIO::set)
            .transform { }

    private fun withLeds(spice: Spice): AonOutcome<Pair<RGB, Set<Led>>> =
        getLeds(spice)
            .transform { spice.colour to it }

    private fun getLeds(spice: Spice): AonOutcome<Set<Led>> =
        spice.id
            .let(spiceSlotSource::get)
            .bind { slot ->
                ledGroupSource.get(slot)
                    .failIf(Set<*>::isEmpty) { slot.NotFound() }
            }

    private fun toMap(pairs: List<Pair<RGB, Set<Led>>>): Map<RGB, Set<Led>> =
        pairs.groupBy(Pair<RGB, Set<Led>>::first)
            .mapValues { (_, value) -> value.flatMap { (_, leds) -> leds }.toSet() }

}