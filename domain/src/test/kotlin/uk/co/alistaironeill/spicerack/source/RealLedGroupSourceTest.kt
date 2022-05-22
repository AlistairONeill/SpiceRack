package uk.co.alistaironeill.spicerack.source

import uk.co.alistaironeill.spicerack.repository.InMemoryLedGroupRepository

class RealLedGroupSourceTest : LedGroupSourceTest() {
    override val source = InMemoryLedGroupRepository().let(::RealLedGroupSource)

}