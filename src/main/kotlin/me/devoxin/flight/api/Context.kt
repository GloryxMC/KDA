package me.devoxin.flight.api

import me.devoxin.flight.internal.entities.Executable
import me.devoxin.flight.internal.utils.Scheduler
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.Event
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.requests.restaction.MessageAction
import net.gloryx.kda.*
import java.util.concurrent.TimeoutException
import java.util.regex.Pattern

class Context(
    val commandClient: CommandClient,
    private val event: MessageReceivedEvent,
    val trigger: String,
    val invokedCommand: Executable
) {
    val jda: JDA = event.jda
    val message: Message = event.message

    val author: User = event.author

    val guild: Guild? = if (event.isFromGuild) event.guild else null
    val member: Member? = event.member

    val textChannel: TextChannel? = if (event.isFromType(ChannelType.TEXT)) event.textChannel else null
    val privateChannel: PrivateChannel? = if (event.isFromType(ChannelType.PRIVATE)) event.privateChannel else null
    val messageChannel: MessageChannel = event.channel



    /**
     * Reply to the message.
     *
     * This makes use of [SendDefaults].
     *
     * @param[content] The message content
     * @param[embed] One embed
     * @param[embeds] Multiple embeds
     * @param[components] Components for this message
     * @param[file] One file
     * @param[files] Multiple files
     *
     * @return[MessageAction]
     */
    fun reply(
            content: String? = SendDefaults.content,
            embed: MessageEmbed? = null,
            embeds: Embeds = SendDefaults.embeds,
            components: Components = SendDefaults.components,
            file: NamedFile? = null,
            files: Files = emptyList(),
    ) = message.reply_(content, embed, embeds, components, file, files)


    /**
     * Send a message to the author.
     *
     * This makes use of [SendDefaults].
     *
     * @param[content] The message content
     * @param[embed] One embed
     * @param[embeds] Multiple embeds
     * @param[components] Components for this message
     * @param[file] One file
     * @param[files] Multiple files
     *
     * @return[MessageAction]
     */
    suspend fun sendPrivate(
            content: String? = SendDefaults.content,
            embed: MessageEmbed? = null,
            embeds: Embeds = SendDefaults.embeds,
            components: Components = SendDefaults.components,
            file: NamedFile? = null,
            files: Files = emptyList(),
    ): MessageAction = author.openPrivateChannel().await().send(content, embed, embeds, components, file, files)

    /**
     * Send a message in this channel.
     *
     * This makes use of [SendDefaults].
     *
     * @param[content] The message content
     * @param[embed] One embed
     * @param[embeds] Multiple embeds
     * @param[components] Components for this message
     * @param[file] One file
     * @param[files] Multiple files
     *
     * @return[MessageAction]
     */
    fun send(
            content: String? = SendDefaults.content,
            embed: MessageEmbed? = null,
            embeds: Embeds = SendDefaults.embeds,
            components: Components = SendDefaults.components,
            file: NamedFile? = null,
            files: Files = emptyList(),
    ): MessageAction = messageChannel.send(content, embed, embeds, components, file, files)

    /**
     * Sends a typing status within the channel until the provided function is exited.
     *
     * @param whileTyping
     *        The code that should be executed while the typing status is active.
     */
    fun typing(whileTyping: () -> Unit) {
        messageChannel.sendTyping().queue {
            val task = Scheduler.every(5000) {
                messageChannel.sendTyping().queue()
            }
            whileTyping()
            task.cancel(true)
        }
    }

    /**
     * Sends a typing status within the channel until the provided function is exited.
     *
     * @param whileTyping
     *        The code that should be executed while the typing status is active.
     */
    suspend fun typingAsync(whileTyping: suspend () -> Unit) {
        messageChannel.sendTyping().await()
        val task = Scheduler.every(5000) { messageChannel.sendTyping().queue() }

        try {
            whileTyping()
        } finally {
            task.cancel(true)
        }
    }

    /**
     * Waits for the given event. Only events that pass the given predicate will be returned.
     * If the timeout is exceeded with no results then null will be returned.
     *
     * @param predicate
     *        A function that must return a boolean denoting whether the event meets the given criteria.
     *
     * @param timeout
     *        How long to wait, in milliseconds, for the given event type before expiring.
     *
     * @throws java.util.concurrent.TimeoutException
     */
    @Throws(TimeoutException::class)
    suspend fun <T: Event> waitFor(event: Class<T>, predicate: (T) -> Boolean, timeout: Long): T {
        val r = commandClient.waitFor(event, predicate, timeout)
        return r.await()
    }

    /**
     * Cleans a string, sanitizing all forms of mentions (role, channel and user), replacing them with
     * their display-equivalent where possible (For example, <@123456789123456789> becomes @User).
     *
     * For cases where the mentioned entity is not cached by the bot, the mention will be replaced
     * with @invalid-<entity type>.
     *
     * It's recommended that you use this only for sending responses back to a user.
     *
     * @param str
     *        The string to clean.
     *
     * @returns The sanitized string.
     */
    fun cleanContent(str: String): String {
        var content = str.replace("e", "е")
        // We use a russian "e" instead of \u200b as it keeps character count the same.
        val matcher = mentionPattern.matcher(str)

        while (matcher.find()) {
            val entityType = matcher.group("type")
            val entityId = matcher.group("id").toLong()
            val fullEntity = matcher.group("mention")

            when (entityType) {
                "@", "@!" -> {
                    val entity = guild?.getMemberById(entityId)?.effectiveName
                        ?: jda.getUserById(entityId)?.name
                        ?: "invalid-user"
                    content = content.replace(fullEntity, "@$entity")
                }
                "@&" -> {
                    val entity = jda.getRoleById(entityId)?.name ?: "invalid-role"
                    content = content.replace(fullEntity, "@$entity")
                }
                "#" -> {
                    val entity = jda.getTextChannelById(entityId)?.name ?: "invalid-channel"
                    content = content.replace(fullEntity, "#$entity")
                }
            }
        }
        return content
    }

    companion object {
        private val mentionPattern = Pattern.compile("(?<mention><(?<type>@!?|@&|#)(?<id>[0-9]{17,21})>)")
    }
}
