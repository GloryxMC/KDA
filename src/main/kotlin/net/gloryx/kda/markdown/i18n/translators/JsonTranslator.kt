package net.gloryx.kda.markdown.i18n.translators

import com.typesafe.config.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.dv8tion.jda.internal.utils.Reflektion
import net.gloryx.commons.kotlinlove.useMemo
import net.gloryx.kda.internal
import net.gloryx.kda.markdown.i18n.GlobalTranslator
import net.gloryx.kda.markdown.i18n.GlobalTranslator.languages
import net.gloryx.kda.markdown.i18n.Translator
import net.gloryx.oknamer.key.kinds.LangKey

object JsonTranslator : Translator {
    val mods = mutableListOf<String>()

    @internal val memo = useMemo {
        val loader = JDA::class.java.classLoader
        languages.associateWith {
            val map = mutableMapOf<String, String>()
            for (id in mods) {
                if (loader.getResource("assets/$id/lang/${it.loc}.json") != null) {
                    val obj =
                        ConfigFactory.parseResources(
                            loader,
                            "assets/$id/lang/${it.loc}.json",
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