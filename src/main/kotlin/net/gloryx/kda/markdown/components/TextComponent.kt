package net.gloryx.kda.markdown.components

import net.gloryx.kda.markdown.Style

class TextComponent(val text: String) : Component {
    override var style: Style = Style()
    override fun toString(): String = style.apply(text)

    companion object {
        val EMPTY = TextComponent("")
    }
}