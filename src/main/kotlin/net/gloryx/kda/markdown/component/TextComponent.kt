package net.gloryx.kda.markdown.component

import net.gloryx.kda.markdown.style.DiscordStyle

class TextComponent(val text: String) : DiscordComponent<DiscordStyle>() {
    override var style: DiscordStyle = DiscordStyle()
    override fun toString() = style.apply(text)

    companion object {
        val empty = TextComponent("")
    }
}