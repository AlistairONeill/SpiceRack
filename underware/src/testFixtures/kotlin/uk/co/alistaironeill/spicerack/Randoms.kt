package uk.co.alistaironeill.spicerack

fun <T: Any> distinctRandoms(generator: () -> T): Iterable<T> = object : Iterable<T> {
    private val used = mutableSetOf<T>()

    override fun iterator() = object : Iterator<T> {
        override fun hasNext() = true

        override fun next(): T {
            while (true) {
                val attempt = generator()
                if (used.add(attempt)) return attempt
            }
        }
    }
}