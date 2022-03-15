package uk.co.alistaironeill.spicerack.slot

import com.ubertob.kondor.json.JMap
import com.ubertob.kondor.json.JSet
import org.http4k.core.HttpHandler
import org.http4k.core.Method.*
import org.http4k.core.Request
import uk.co.alistaironeill.spicerack.error.AonOutcome
import uk.co.alistaironeill.spicerack.error.UnitOutcome
import uk.co.alistaironeill.spicerack.http.bodyAsJson
import uk.co.alistaironeill.spicerack.http.handle
import uk.co.alistaironeill.spicerack.slot.LedGroupSourceHttpHandler.SLOT

class HttpLedGroupSource(private val handler: HttpHandler): LedGroupSource {
    override fun get(slot: Slot): AonOutcome<Set<Led>> =
        Request(GET, slot.url)
            .run(handler)
            .handle(JSet(JLed))

    override fun get(): AonOutcome<Map<Slot, Set<Led>>> =
        Request(GET, SLOT)
            .run(handler)
            .handle(JMap(JSlotString, JSet(JLed)))

    override fun add(slot: Slot, led: Led): UnitOutcome =
        Request(PUT, slot.url)
            .bodyAsJson(led, JLed)
            .run(handler)
            .handle()

    override fun clear(slot: Slot): UnitOutcome =
        Request(DELETE, slot.url)
            .run(handler)
            .handle()

    private val Slot.url get() = "$SLOT/${x.value}/${y.value}"
}