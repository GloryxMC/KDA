package net.gloryx.kda.markdown.translation

import net.dv8tion.jda.api.interactions.DiscordLocale
import net.kyori.adventure.key.Key

interface Translator {
    fun translate(key: Key, locale: DiscordLocale): String?
}