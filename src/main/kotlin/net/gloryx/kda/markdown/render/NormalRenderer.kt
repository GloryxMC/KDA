package net.gloryx.kda.markdown.render

import net.gloryx.kda.markdown.components.TextComponent
import net.gloryx.kda.markdown.i18n.Env

object NormalRenderer : ComponentRenderer<TextComponent>() {
    override fun render(component: TextComponent, env: Env): String = component.toString()
}