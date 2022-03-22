package uk.co.alistaironeill.spicerack.spice

data class RGB(val red: Byte, val green: Byte, val blue: Byte) {
    companion object {
        val Default = RGB(0, 127, 0)
    }
}