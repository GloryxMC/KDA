package net.gloryx.kda.markdown.component

import net.gloryx.kda.markdown.style.DiscordStyle
import net.kyori.adventure.key.Key

class TranslationComponent(val key: Key) : DiscordComponent<DiscordStyle>() {
    override var style: DiscordStyle = DiscordStyle()
}