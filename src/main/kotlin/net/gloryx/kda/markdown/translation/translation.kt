package net.gloryx.kda.markdown.translation

import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.gloryx.commons.kotlinlove.safeCast
import net.gloryx.kda.guildOrNull
import net.gloryx.kda.markdown.component.ComponentRenderer
import net.gloryx.kda.markdown.component.DiscordComponent
import net.gloryx.kda.markdown.component.TranslationComponent
import net.gloryx.kda.markdown.translation.TranslationPref.LanguageProvider
import net.gloryx.kda.send
import net.gloryx.oknamer.key.Key
import net.gloryx.oknamer.key.kinds.LangKey

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

fun MessageChannel.send(component: DiscordComponent) = send(
    component, TranslationPref.criteria.getLanguage(
        LanguageProvider.Context(jda.selfUser, safeCast<GuildChannel>()?.guild)
    )
)
fun MessageChannel.send(key: Key) = send(TranslationComponent(key))

fun MessageChannel.send(key: Key, locale: DiscordLocale) = send(TranslationComponent(key), locale)
fun MessageChannel.send(component: DiscordComponent, locale: DiscordLocale) =
    send(ComponentRenderer.render(component, locale))

fun DiscordLocale.render(key: Key): String? =
    GlobalTranslator.translate(key, this)

fun tr(key: Key) = TranslationComponent(key)
fun tr(key: String) = key.split('.').run { subList(1, size - 1) }
    .let { split -> TranslationComponent(LangKey(split[0], split[1])) }