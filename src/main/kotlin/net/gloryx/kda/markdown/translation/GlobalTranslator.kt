package net.gloryx.kda.markdown.translation

import net.dv8tion.jda.api.interactions.DiscordLocale
import net.gloryx.kda.markdown.component.TextComponent
import net.gloryx.kda.markdown.component.TranslationComponent
import net.kyori.adventure.key.Key

object GlobalTranslator : Translator {
    private val translators: MutableList<Translator> = mutableListOf()

    fun register(translator: Translator) { if (!translators.contains(translator)) translators.add(translator) }

    override fun translate(key: Key, locale: DiscordLocale): String? = translators.map { it.translate(key, locale) }.firstOrNull()

    fun render(component: TranslationComponent, locale: DiscordLocale) = TextComponent(translate(component.key, locale) ?: "").apply { style = component.style }
}