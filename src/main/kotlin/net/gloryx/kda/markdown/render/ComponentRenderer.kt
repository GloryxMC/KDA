package net.gloryx.kda.markdown.render

import net.dv8tion.jda.api.interactions.DiscordLocale
import net.gloryx.kda.markdown.components.Component
import net.gloryx.kda.markdown.components.TextComponent
import net.gloryx.kda.markdown.components.TranslationComponent
import net.gloryx.kda.markdown.i18n.Env
import net.gloryx.kda.markdown.i18n.GlobalTranslator
import net.gloryx.kda.markdown.i18n.LanguageYeeter

abstract class ComponentRenderer<C : Component> {
    open fun render(component: C, env: Env): String = when (component) {
        is TranslationComponent -> TranslatableRenderer.render(component, env)
        else -> component.toString()
    }

    companion object : ComponentRenderer<Component>() {

    }
}