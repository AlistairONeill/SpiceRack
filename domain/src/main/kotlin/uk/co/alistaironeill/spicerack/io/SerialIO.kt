package uk.co.alistaironeill.spicerack.io

interface SerialIO {
    fun send(commands: List<SerialCommand>)
}