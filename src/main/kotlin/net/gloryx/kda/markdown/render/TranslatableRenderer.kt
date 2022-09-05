package net.gloryx.kda.markdown.render

import net.gloryx.kda.markdown.components.TranslationComponent
import net.gloryx.kda.markdown.i18n.Env
import net.gloryx.kda.markdown.i18n.GlobalTranslator
import net.gloryx.kda.markdown.i18n.LanguageYeeter

object TranslatableRenderer : ComponentRenderer<TranslationComponent>() {
    override fun render(component: TranslationComponent, env: Env): String =
        NormalRenderer.render(GlobalTranslator.renderToComponent(component, LanguageYeeter.criteria.yeet(env)), env)
}