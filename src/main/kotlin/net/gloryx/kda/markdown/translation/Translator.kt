package net.gloryx.kda.markdown.translation

import net.dv8tion.jda.api.interactions.DiscordLocale
import net.gloryx.oknamer.key.kinds.LangKey

interface Translator {
    fun translate(key: LangKey, locale: DiscordLocale): String?
}