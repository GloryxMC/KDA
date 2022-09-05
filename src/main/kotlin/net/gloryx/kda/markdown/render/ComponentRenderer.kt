package net.gloryx.kda.markdown.render

import net.gloryx.kda.markdown.components.Component
import net.gloryx.kda.markdown.components.TranslationComponent
import net.gloryx.kda.markdown.i18n.Env

abstract class ComponentRenderer<C : Component> {
    open fun render(component: C, env: Env): String = when (component) {
        is TranslationComponent -> TranslatableRenderer.render(component, env)
        else -> component.toString()
    }

    companion object : ComponentRenderer<Component>() {

    }
}