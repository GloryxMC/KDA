package net.gloryx.kda.markdown.component

import net.gloryx.kda.markdown.style.DiscordStyle
import net.gloryx.oknamer.key.Key
import net.gloryx.oknamer.key.Keyed
import net.gloryx.oknamer.key.kinds.LangKey

class TranslationComponent(val key: LangKey) : DiscordComponent() {
    constructor(keyed: Keyed) : this(LangKey(keyed.key.namespace, keyed.key.value))
    override var style: DiscordStyle = DiscordStyle()
}