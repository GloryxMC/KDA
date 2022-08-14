package net.gloryx.kda.markdown

import org.jetbrains.annotations.ApiStatus.NonExtendable

@NonExtendable
fun interface Decoration {
    fun apply(text: String): String
}

fun String.around(around: String) = "$around$this$around"