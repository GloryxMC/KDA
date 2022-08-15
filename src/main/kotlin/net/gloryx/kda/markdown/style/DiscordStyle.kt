package net.gloryx.kda.markdown.style

import net.gloryx.kda.markdown.Decoration
import org.jetbrains.annotations.ApiStatus.NonExtendable

@NonExtendable
open class DiscordStyle internal constructor(private val list: List<Decoration>) {
    constructor() : this(listOf())
    fun add(decoration: Decoration) = copy(list + decoration)

    private fun copy(list: List<Decoration>) = DiscordStyle(list)

    internal fun apply(text: String) = list.fold(text) { t, it -> it.apply(t) }

    companion object {

    }
}

interface Styled<S : DiscordStyle> {
    var style: S
}