package uk.co.alistaironeill.spicerack.io

class StubSerialIO : SerialIO {
    val received = mutableListOf<SerialCommand>()

    override fun send(commands: List<SerialCommand>) {
        received.addAll(commands)
    }
}