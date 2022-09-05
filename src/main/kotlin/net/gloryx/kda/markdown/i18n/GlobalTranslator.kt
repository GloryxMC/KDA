package net.gloryx.kda.markdown.i18n

import net.dv8tion.jda.api.interactions.DiscordLocale
import net.gloryx.kda.markdown.components.TextComponent
import net.gloryx.kda.markdown.components.TranslationComponent
import net.gloryx.oknamer.key.kinds.LangKey

object GlobalTranslator : Translator {
    var DEFAULT_LOCALE = DiscordLocale.ENGLISH_US
    val languages = mutableListOf(DiscordLocale.ENGLISH_US)
    private val translators: MutableList<Translator> = mutableListOf()

    fun register(translator: Translator) {
        if (!translators.contains(translator)) translators.add(translator)
    }

    override fun translate(key: LangKey, locale: DiscordLocale): String? =
        translators.firstNotNullOfOrNull { it.translate(key, locale) }

    fun renderToComponent(component: TranslationComponent, locale: DiscordLocale) =
        TextComponent(String.format((translate(component.key, locale) ?: translate(component.key, DEFAULT_LOCALE)) ?: component.key.asString(), *component.args)).apply { style = component.style }
}