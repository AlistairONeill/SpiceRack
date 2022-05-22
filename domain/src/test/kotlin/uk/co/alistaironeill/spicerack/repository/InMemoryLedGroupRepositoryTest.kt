package uk.co.alistaironeill.spicerack.repository

class InMemoryLedGroupRepositoryTest : LedGroupRepositoryTest() {
    override val repository = InMemoryLedGroupRepository()
}