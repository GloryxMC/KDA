package net.gloryx.kda.adventure

data class Namespace(val namespace: String) {
    fun key(value: String) = Key.new(this, value)
}