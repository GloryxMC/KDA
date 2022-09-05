package net.gloryx.kda.markdown.i18n

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.dv8tion.jda.internal.utils.Mocks

data class Env(val user: User, val guild: Guild?) {
    val member: Member? get() = user?.let { guild?.getMember(it) }
    val isGuild get() = guild != null && member != null
    var locale: DiscordLocale? = null
    operator fun component3() = locale

    constructor(locale: DiscordLocale) : this(Mocks().user(), null) {
        this.locale = locale
    }
}

object LanguageYeeter {
    var criteria: I18nYeeter = I18nYeeter { (_, g, l) -> (g?.locale ?: l) ?: DiscordLocale.ENGLISH_US }
}

fun interface I18nYeeter {
    fun yeet(env: Env): DiscordLocale
}