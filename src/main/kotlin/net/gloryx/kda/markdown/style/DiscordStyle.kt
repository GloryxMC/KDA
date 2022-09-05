package net.gloryx.kda.markdown.style

import net.gloryx.kda.internal
import net.gloryx.kda.markdown.Decoration
import org.jetbrains.annotations.ApiStatus.Internal
import org.jetbrains.annotations.ApiStatus.NonExtendable

@NonExtendable
open class DiscordStyle @internal constructor(@internal val list: List<Decoration>) {
    constructor() : this(listOf())

    fun add(decoration: Decoration) = copy(list + decoration)

    private fun copy(list: List<Decoration>) = DiscordStyle(list)

    @internal fun apply(text: String) = list.fold(text) { t, it -> it.apply(t) }

    companion object {
        val EMPTY = DiscordStyle(listOf())
    }
}

interface Styled<S : DiscordStyle> {
    var style: S
}