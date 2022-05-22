package uk.co.alistaironeill.spicerack.slot

import uk.co.alistaironeill.spicerack.http.asHttpHandler
import uk.co.alistaironeill.spicerack.repository.InMemoryLedGroupRepository
import uk.co.alistaironeill.spicerack.slot.LedGroupSourceHttpHandler.toContractRoutes
import uk.co.alistaironeill.spicerack.source.LedGroupSourceTest
import uk.co.alistaironeill.spicerack.source.RealLedGroupSource

class HttpLedGroupSourceTest : LedGroupSourceTest() {
    override val source = InMemoryLedGroupRepository()
        .let(::RealLedGroupSource)
        .toContractRoutes()
        .asHttpHandler()
        .let(::HttpLedGroupSource)
}