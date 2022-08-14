package net.gloryx.kda.markdown.component

import net.gloryx.kda.markdown.style.DiscordStyle
import net.gloryx.kda.markdown.style.Styled

sealed class DiscordComponent<S : DiscordStyle> : Styled<S> {
    companion object {
        fun empty() = TextComponent.empty
    }
}