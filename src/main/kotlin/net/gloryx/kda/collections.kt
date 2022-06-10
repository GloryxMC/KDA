package net.gloryx.kda

fun <K, V> Map<K, V>.replaceOrPut(key: K, value: V) = toMutableMap().apply {
    replace(key, value) ?: put(key, value)
}
