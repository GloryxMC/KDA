package net.gloryx.kda.markdown.component

import net.dv8tion.jda.api.interactions.DiscordLocale
import net.gloryx.commons.kotlinlove.cast
import net.gloryx.kda.markdown.style.DiscordStyle
import net.gloryx.kda.markdown.translation.GlobalTranslator
import net.gloryx.kda.markdown.translation.render
import java.util.Locale

abstract class ComponentRenderer {
    fun render(component: DiscordComponent<out DiscordStyle>, vararg args: Array<out Any>): String = when (component) {
        is TextComponent -> component.toString()
        is TranslationComponent -> renderText(renderTranslatable(component, args[0].cast()))
    }

    open fun renderText(component: TextComponent) = component.toString()
    open fun renderTranslatable(component: TranslationComponent, locale: DiscordLocale): TextComponent =
        Translatable.renderTranslatable(component, locale) // delegate to the designated renderer.

    companion object : ComponentRenderer() {
        fun render(component: DiscordComponent<out DiscordStyle>, vararg args: Any) = render(component, args)
    }

    object Translatable : ComponentRenderer() {
        override fun renderTranslatable(component: TranslationComponent, locale: DiscordLocale): TextComponent =
            locale.render(component.key)?.let(::TextComponent) ?: TextComponent.empty
    }
}