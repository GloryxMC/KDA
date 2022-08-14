package net.gloryx.kda.markdown.translation

import com.typesafe.config.ConfigFactory
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.gloryx.commons.kotlinlove.useMemo
import net.gloryx.kda.adventure.DiscordComponentSerializer
import net.gloryx.kda.markdown.translation.TranslationPref.LanguageProvider
import net.gloryx.kda.guildOrNull
import net.gloryx.kda.markdown.component.ComponentRenderer
import net.gloryx.kda.markdown.component.DiscordComponent
import net.gloryx.kda.markdown.component.TranslationComponent
import net.gloryx.kda.markdown.translation.GlobalTranslator.render
import net.gloryx.kda.send
import net.kyori.adventure.key.Key
import java.io.File
import java.util.Locale

object TranslationPref {
    var criteria: LanguageProvider = LanguageProvider { (_, g) -> g?.locale ?: DiscordLocale.ENGLISH_US }

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

fun MessageChannel.send(key: Key, locale: DiscordLocale) = send(TranslationComponent(key), locale)
fun MessageChannel.send(component: DiscordComponent, locale: DiscordLocale) = send(ComponentRenderer.render(component, locale))

fun DiscordLocale.render(key: Key): String? =
    GlobalTranslator.translate(key, this)

fun Key.render(renderType: KeyRenderType): String = renderType.renderer(this)

enum class KeyRenderType(val renderer: (Key) -> String) {
    MINECRAFT(Key::asString),
    LANG({ "${it.namespace()}.${it.value()}" })
}

fun tr(key: Key) = TranslationComponent(key)
fun tr(key: String) = TranslationComponent(Key.key(key, '.'))