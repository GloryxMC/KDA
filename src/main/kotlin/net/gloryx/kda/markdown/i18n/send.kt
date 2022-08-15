package net.gloryx.kda.markdown.i18n

import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.dv8tion.jda.api.requests.restaction.MessageAction
import net.gloryx.commons.reflect.safeCast
import net.gloryx.kda.markdown.components.Component
import net.gloryx.kda.markdown.components.TranslationComponent
import net.gloryx.kda.markdown.render.ComponentRenderer
import net.gloryx.kda.markdown.render.TranslatableRenderer

fun MessageChannel.send(component: Component): MessageAction =
    sendMessage(ComponentRenderer.render(component, Env(jda.selfUser, safeCast<GuildChannel>()?.guild)))

fun MessageChannel.send(component: TranslationComponent, locale: DiscordLocale) =
    sendMessage(TranslatableRenderer.render(component, Env(locale)))

fun MessageChannel.send(component: TranslationComponent, criteria: I18nYeeter): MessageAction =
    send(component, criteria.yeet(Env(jda.selfUser, safeCast<GuildChannel>()?.guild)))