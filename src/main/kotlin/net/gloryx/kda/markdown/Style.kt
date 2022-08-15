package net.gloryx.kda.markdown

import org.jetbrains.annotations.ApiStatus.NonExtendable

@NonExtendable
open class Style internal constructor(private val list: List<Decoration>) {
    constructor() : this(listOf())

    fun add(decoration: Decoration) = copy(list + decoration)

    private fun copy(list: List<Decoration>) = Style(list)

    internal fun apply(text: String) = list.fold(text) { t, it -> it.apply(t) }

    companion object {

    }
}

interface Styled<S : Style> {
    var style: S
}