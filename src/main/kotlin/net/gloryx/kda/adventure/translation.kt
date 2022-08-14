package net.gloryx.kda.adventure

import com.typesafe.config.ConfigFactory
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.gloryx.commons.kotlinlove.useMemo
import net.gloryx.kda.adventure.TranslationPref.LanguageProvider
import net.gloryx.kda.guildOrNull
import net.gloryx.kda.send
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.translation.GlobalTranslator
import net.kyori.adventure.translation.TranslationRegistry
import net.kyori.adventure.translation.Translator
import java.io.File
import java.util.Locale

object TranslationPref {
    var criteria: LanguageProvider = LanguageProvider { (_, g) -> g?.locale ?: DiscordLocale.ENGLISH_US }
    val localePaths = mutableListOf<String>()
    val all = useMemo {
        val tr = mutableMapOf<Locale, MutableMap<String, String>>()

        for (sp in localePaths) {
            val file = File(sp).listFiles() ?: continue
            val conf = ConfigFactory.load()
        }
    }

    fun render(key: Key, locale: DiscordLocale) = GlobalTranslator.render(Component.translatable(key.render(KeyRenderType.LANG)), locale.java)
    fun interface LanguageProvider {
        fun getLanguage(context: Context): DiscordLocale

        data class Context(val user: User, val guild: Guild?) {
            val inGuild: Boolean get() = guild != null && member != null
            val member: Member? get() = guild?.getMember(user)

            companion object {
                fun of(message: Message) = Context(message.author, message.guildOrNull)
            }
        }
    }
}

fun MessageChannel.send(key: Key, locale: TranslationPref.LanguageProvider = TranslationPref.criteria) = send(locale.getLanguage(
    LanguageProvider.Context(jda.selfUser, (this as? GuildChannel)?.guild)).render(key))

fun DiscordLocale.render(key: Key): String = DiscordComponentSerializer.serialize(GlobalTranslator.render(tr(key), java))

fun Key.render(renderType: KeyRenderType): String = renderType.renderer(this)

enum class KeyRenderType(val renderer: (Key) -> String) {
    MINECRAFT(Key::asString),
    LANG({ "${it.namespace()}.${it.value()}" })
}

fun tr(key: Key) = Component.translatable(key.render(KeyRenderType.LANG))