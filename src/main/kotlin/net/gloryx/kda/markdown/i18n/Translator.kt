package net.gloryx.kda.markdown.i18n

import net.dv8tion.jda.api.interactions.DiscordLocale
import net.gloryx.oknamer.key.kinds.LangKey

interface Translator {
    fun translate(key: LangKey, locale: DiscordLocale): String?
}