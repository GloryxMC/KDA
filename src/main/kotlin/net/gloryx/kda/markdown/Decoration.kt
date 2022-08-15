package net.gloryx.kda.markdown

fun interface Decoration {
    fun apply(text: String): String
}