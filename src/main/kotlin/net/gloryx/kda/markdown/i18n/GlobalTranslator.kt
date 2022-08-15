package net.gloryx.kda.markdown.i18n

import com.fasterxml.jackson.annotation.Nulls
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.dv8tion.jda.internal.utils.Helpers
import net.gloryx.kda.markdown.components.TextComponent
import net.gloryx.kda.markdown.components.TranslationComponent
import net.gloryx.kda.markdown.render.ComponentRenderer
import net.gloryx.oknamer.key.kinds.LangKey

object GlobalTranslator : Translator {
    private val translators: MutableList<Translator> = mutableListOf()

    fun register(translator: Translator) { if (!translators.contains(translator)) translators.add(translator) }

    override fun translate(key: LangKey, locale: DiscordLocale): String? = translators.map { it.translate(key, locale) }.firstOrNull()

    fun render(component: TranslationComponent, locale: DiscordLocale) = ComponentRenderer.render(component, Env(locale))
}