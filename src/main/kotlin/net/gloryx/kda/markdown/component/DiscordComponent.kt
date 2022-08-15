package net.gloryx.kda.markdown.component

import net.gloryx.kda.markdown.style.DiscordStyle
import net.gloryx.kda.markdown.style.Styled

sealed class DiscordComponent : Styled<DiscordStyle> {
    companion object {
        fun empty() = TextComponent.empty
    }
}