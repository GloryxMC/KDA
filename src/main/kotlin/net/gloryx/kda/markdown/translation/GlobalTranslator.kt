package net.gloryx.kda.markdown.translation

import net.dv8tion.jda.api.interactions.DiscordLocale
import net.gloryx.kda.markdown.component.TextComponent
import net.gloryx.kda.markdown.component.TranslationComponent
import net.gloryx.oknamer.key.Key
import net.gloryx.oknamer.key.kinds.LangKey

object GlobalTranslator : Translator {
    private val translators: MutableList<Translator> = mutableListOf()

    fun register(translator: Translator) { if (!translators.contains(translator)) translators.add(translator) }

    override fun translate(key: LangKey, locale: DiscordLocale): String? = translators.map { it.translate(key, locale) }.firstOrNull()

    fun render(component: TranslationComponent, locale: DiscordLocale) = TextComponent(translate(component.key, locale) ?: "").apply { style = component.style }
}