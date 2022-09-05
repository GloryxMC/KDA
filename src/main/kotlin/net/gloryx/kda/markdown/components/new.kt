package net.gloryx.kda.markdown.components

import net.gloryx.oknamer.key.kinds.LangKey

fun tr(key: LangKey, vararg args: Any?) = TranslationComponent(key, args)
fun tr(key: String, vararg args: Any?) =
    key.replaceFirst('.', ':').split(':', limit = 2).let { arr -> TranslationComponent(LangKey(arr[0], arr[1]), args) }