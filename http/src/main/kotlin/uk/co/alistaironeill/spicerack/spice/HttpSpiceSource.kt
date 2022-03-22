package uk.co.alistaironeill.spicerack.spice

import com.ubertob.kondor.json.JSet
import org.http4k.core.HttpHandler
import org.http4k.core.Method.*
import org.http4k.core.Request
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.http.bodyAsJson
import uk.co.alistaironeill.spicerack.http.handle
import uk.co.alistaironeill.spicerack.spice.SpiceSourceHttpHandler.SPICE_NAME_PATH
import uk.co.alistaironeill.spicerack.spice.SpiceSourceHttpHandler.SPICE_PATH
import uk.co.alistaironeill.spicerack.spice.SpiceUpdate.*

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
        id.post(AddAlias(name))

    override fun removeAlias(id: SpiceId, name: SpiceName): UnitOutcome =
        id.post(RemoveAlias(name))

    override fun rename(id: SpiceId, name: SpiceName): UnitOutcome =
        id.post(Rename(name))

    override fun delete(id: SpiceId): UnitOutcome =
        Request(DELETE, "$SPICE_PATH/${id.value}")
            .run(handler)
            .handle()

    private fun SpiceId.post(update: SpiceUpdate) : UnitOutcome =
        Request(POST, "$SPICE_PATH/$value")
            .bodyAsJson(update, JSpiceUpdate)
            .run(handler)
            .handle()
}