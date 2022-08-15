package net.gloryx.kda.markdown.translation.translators

import com.typesafe.config.*
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.dv8tion.jda.internal.utils.Reflektion
import net.gloryx.commons.kotlinlove.useMemo
import net.gloryx.kda.markdown.translation.GlobalTranslator
import net.gloryx.kda.markdown.translation.Translator
import net.gloryx.kda.markdown.translation.render
import net.gloryx.oknamer.key.Key
import net.gloryx.oknamer.key.kinds.LangKey

object HangTranslator : Translator {
    private var languages: List<DiscordLocale> = listOf(DiscordLocale.ENGLISH_US)

    val mods = mutableListOf<String>()

    private val memo = useMemo {
        val loader = Reflektion.getCalleeClass().classLoader
        languages.associateWith {
            val map = mutableMapOf<String, String>()
            for (id in mods) {
                if (loader.getResource("assets/$id/lang/${it.locale}.conf") != null) {
                    val obj =
                        ConfigFactory.parseResources(
                            loader,
                            "assets/$id/lang/${it.locale}.conf",
                            ConfigParseOptions.defaults()
                        ).root()
                    for ((key, value) in obj) {
                        recObj(key, value, map)
                    }
                }
            }
            map.toMap()
        }
    }
    private var translations by memo

    fun use() {
        GlobalTranslator.register(this)
    }

    override fun translate(key: LangKey, locale: DiscordLocale): String? =
        translations[locale]?.get(key.asString()) ?: run {
            memo.refresh()
            languages = languages + locale
            translations[locale]?.get(key.asString())
        }

    private fun recObj(key: String, obj: ConfigValue, map: MutableMap<String, String>) {
        if (obj.valueType() == ConfigValueType.STRING) map[key] = obj.unwrapped() as String
        else {
            for (o in (obj as ConfigObject)) {
                if (o.value.valueType() == ConfigValueType.OBJECT) recObj("$key.${o.key}", o.value, map)
                else if (o.value.valueType() == ConfigValueType.STRING) map["$key.${o.key}"] =
                    o.value.unwrapped() as String
            }
        }
    }
}