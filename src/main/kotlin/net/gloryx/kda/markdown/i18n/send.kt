package net.gloryx.kda.markdown.i18n

import me.devoxin.flight.api.Context
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.marker.HasGuild
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.dv8tion.jda.api.requests.restaction.MessageAction
import net.gloryx.commons.reflect.safeCast
import net.gloryx.kda.guildOrNull
import net.gloryx.kda.markdown.components.Component
import net.gloryx.kda.markdown.components.TranslationComponent
import net.gloryx.kda.markdown.render.ComponentRenderer
import net.gloryx.kda.markdown.render.TranslatableRenderer

fun MessageChannel.send(component: Component): MessageAction =
    sendMessage(ComponentRenderer.render(component, Env(jda.selfUser, safeCast<GuildChannel>()?.guild)))

fun MessageChannel.send(component: TranslationComponent, locale: DiscordLocale) =
    sendMessage(GlobalTranslator.renderToComponent(component, locale).toString())

fun MessageChannel.send(component: TranslationComponent, criteria: I18nYeeter): MessageAction =
    send(component, criteria.yeet(Env(jda.selfUser, safeCast<GuildChannel>()?.guild)))
fun Message.reply(component: TranslationComponent) = reply(component.render(env))

fun Context.reply(component: TranslationComponent): MessageAction = reply(component.render(env))
fun Context.send(component: TranslationComponent): MessageAction = send(component.render(env))

val Message.env get() = Env(author, guildOrNull)

fun Component.render(env: Env) = ComponentRenderer.render(this, env)