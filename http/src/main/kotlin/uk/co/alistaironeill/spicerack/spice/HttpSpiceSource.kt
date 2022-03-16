package uk.co.alistaironeill.spicerack.spice

import com.ubertob.kondor.json.JSet
import org.http4k.core.HttpHandler
import org.http4k.core.Method.*
import org.http4k.core.Request
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.http.handle
import uk.co.alistaironeill.spicerack.spice.SpiceSourceHttpHandler.SPICE_NAME_PATH
import uk.co.alistaironeill.spicerack.spice.SpiceSourceHttpHandler.SPICE_PATH

class HttpSpiceSource(private val handler: HttpHandler): SpiceSource {
    override fun get(): AonOutcome<Set<Spice>> =
        Request(GET, SPICE_PATH)
            .run(handler)
            .handle(JSet(JSpice))

    override fun get(id: SpiceId): AonOutcome<Spice> =
        Request(GET, "$SPICE_PATH/${id.value}")
            .run(handler)
            .handle(JSpice)

    override fun get(name: SpiceName): AonOutcome<Spice> =
        Request(GET, "$SPICE_NAME_PATH/${name.value}")
            .run(handler)
            .handle(JSpice)

    override fun create(name: SpiceName): AonOutcome<Spice> =
        Request(PUT, "$SPICE_NAME_PATH/${name.value}")
            .run(handler)
            .handle(JSpice)

    override fun addAlias(id: SpiceId, name: SpiceName): UnitOutcome =
        Request(PUT, "$SPICE_PATH/${id.value}/${name.value}")
            .run(handler)
            .handle()

    override fun removeAlias(id: SpiceId, name: SpiceName): UnitOutcome =
        Request(DELETE, "$SPICE_PATH/${id.value}/${name.value}")
            .run(handler)
            .handle()

    override fun remove(id: SpiceId): UnitOutcome =
        Request(DELETE, "$SPICE_PATH/${id.value}")
            .run(handler)
            .handle()
}