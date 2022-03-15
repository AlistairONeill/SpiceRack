package uk.co.alistaironeill.spicerack.slot

import uk.co.alistaironeill.spicerack.http.asHttpHandler
import uk.co.alistaironeill.spicerack.slot.LedGroupSourceHttpHandler.toContractRoutes

class HttpLedGroupSourceTest : LedGroupSourceTest() {
    override val source = InMemoryLedGroupSource()
        .toContractRoutes()
        .asHttpHandler()
        .let(::HttpLedGroupSource)
}