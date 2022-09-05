package net.gloryx.kda.markdown.components

import net.dv8tion.jda.api.interactions.DiscordLocale
import net.gloryx.kda.markdown.Style
import net.gloryx.kda.markdown.i18n.Env
import net.gloryx.kda.markdown.i18n.GlobalTranslator
import net.gloryx.kda.markdown.render.ComponentRenderer
import net.gloryx.oknamer.key.kinds.LangKey

class TranslationComponent(val key: LangKey, val args: Array<out Any?> = arrayOf()) : Component {
    override var style: Style = Style()

    operator fun get(locale: DiscordLocale) = ComponentRenderer.render(this, Env(locale))
}
