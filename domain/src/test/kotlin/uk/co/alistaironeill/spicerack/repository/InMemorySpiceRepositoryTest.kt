package uk.co.alistaironeill.spicerack.repository

class InMemorySpiceRepositoryTest : SpiceRepositoryTest() {
    override val repository = InMemorySpiceRepository()
}