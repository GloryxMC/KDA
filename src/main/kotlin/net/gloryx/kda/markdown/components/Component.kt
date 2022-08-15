package net.gloryx.kda.markdown.components

import net.gloryx.kda.markdown.Style
import net.gloryx.kda.markdown.Styled
import org.jetbrains.annotations.ApiStatus.NonExtendable

@NonExtendable
interface Component : Styled<Style> {
    companion object {
        val EMPTY = TextComponent.EMPTY
    }
}