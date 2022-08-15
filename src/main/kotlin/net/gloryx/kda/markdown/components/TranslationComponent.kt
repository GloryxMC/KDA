package net.gloryx.kda.markdown.components

import net.gloryx.kda.markdown.Style
import net.gloryx.oknamer.key.kinds.LangKey

class TranslationComponent(val key: LangKey) : Component {
    override var style: Style = Style()
}
