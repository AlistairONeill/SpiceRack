package uk.co.alistaironeill.spicerack.collections

fun <K: Any, V: Any> Map<K, V>.invert(): Map<V, Set<K>> =
    entries
        .groupBy(::value, ::key)
        .mapValues(::toSet)

private fun <K: Any, V: Any> value(entry: Map.Entry<K, V>) : V = entry.value
private fun <K: Any, V: Any> key(entry: Map.Entry<K, V>) : K = entry.key
private fun <K: Any, V: Any> toSet(entry: Map.Entry<K, List<V>>) : Set<V> = entry.value.toSet()