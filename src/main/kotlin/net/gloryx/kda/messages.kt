package net.gloryx.kda

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.interactions.InteractionHook
import net.dv8tion.jda.api.interactions.callbacks.IMessageEditCallback
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.requests.restaction.MessageAction
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction
import net.dv8tion.jda.api.requests.restaction.WebhookMessageUpdateAction
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction
import net.dv8tion.jda.internal.requests.restaction.MessageActionImpl
import net.gloryx.commons.try_

/**
 * Defaults used for edit message extensions provided by this module.
 * Each function that relies on these defaults, says so explicitly. This does not apply for send functions.
 *
 * These can be changed but keep in mind they will apply globally.
 */
object MessageEditDefaults {
    /**
     * Whether message edits should replace the entire message by default
     */
    var replace: Boolean = false
}

/**
 * Add a collection of [NamedFile] to this request.
 *
 * @param[files] The files to add
 */
fun MessageEditCallbackAction.addFiles(files: Files) {
    files.forEach {
        addFile(it.data, it.name, *it.options)
    }
}

/**
 * Add a collection of [NamedFile] to this request.
 *
 * @param[files] The files to add
 */
fun <T> WebhookMessageUpdateAction<T>.addFiles(files: Files) {
    files.forEach {
        addFile(it.data, it.name, *it.options)
    }
}


// Using an underscore at the end to prevent overload specialization
// You can remove it with an import alias (import ... as foo) but i would recommend against it

private inline fun <T> T.applyIf(check: Boolean, func: (T) -> Unit) {
    if (check || this != null) {
        func(this)
    }
}

/**
 * Edit the original message from this interaction.
 *
 * This makes use of [MessageEditDefaults].
 *
 * This does not currently replace files but may do so in the future.
 *
 * @param[content] The message content
 * @param[embed] One embed
 * @param[embeds] Multiple embeds
 * @param[components] Components for this message
 * @param[file] One file
 * @param[files] Multiple files
 * @param[replace] Whether this should replace the entire message
 *
 * @return[MessageEditCallbackAction]
 */
fun IMessageEditCallback.editMessage_(
        content: String? = null,
        embed: MessageEmbed? = null,
        embeds: Embeds? = null,
        components: Components? = null,
        file: NamedFile? = null,
        files: Files? = null,
        replace: Boolean = MessageEditDefaults.replace
): MessageEditCallbackAction = deferEdit().apply {
    content.applyIf(replace) {
        setContent(it)
    }

    components.applyIf(replace) {
        setActionRows(it?.mapNotNull { k -> k as? ActionRow } ?: emptyList())
    }

    allOf(embed, embeds).applyIf(replace) {
        setEmbeds(it ?: emptyList())
    }

    allOf(file, files).applyIf(true) {
        if (it != null) {
            addFiles(it)
            if (replace)
                retainFilesById(LongRange(0, it.size.toLong()).map(Long::toString))
        }
        else if (replace) {
            retainFilesById(emptyList())
        }
    }
}

/**
 * Edit a message from this interaction.
 *
 * This makes use of [MessageEditDefaults].
 *
 * This does not currently replace files but may do so in the future.
 *
 * @param[id] The message id, defaults to "@original"
 * @param[content] The message content
 * @param[embed] One embed
 * @param[embeds] Multiple embeds
 * @param[components] Components for this message
 * @param[file] One file
 * @param[files] Multiple files
 * @param[replace] Whether this should replace the entire message
 *
 * @return[WebhookMessageUpdateAction]
 */
fun InteractionHook.editMessage(
        id: String = "@original",
        content: String? = null,
        embed: MessageEmbed? = null,
        embeds: Embeds? = null,
        components: Components? = null,
        file: NamedFile? = null,
        files: Files? = null,
        replace: Boolean = MessageEditDefaults.replace,
): WebhookMessageUpdateAction<Message> = editMessageById(id, "tmp").apply {
    setContent(null)
    content.applyIf(replace) {
        setContent(it)
    }

    components.applyIf(replace) {
        setActionRows(it?.mapNotNull { k -> k as? ActionRow } ?: emptyList())
    }

    allOf(embed, embeds).applyIf(replace) {
        setEmbeds(it ?: emptyList())
    }

    allOf(file, files).applyIf(true) {
        if (it != null) {
            addFiles(it)
            if (replace)
                retainFilesById(LongRange(0, it.size.toLong()).map(Long::toString))
        }
        else if (replace) {
            retainFilesById(emptyList())
        }
    }
}

/**
 * Edit a message from this channel.
 *
 * This makes use of [MessageEditDefaults].
 *
 * This does not currently replace files but may do so in the future.
 *
 * @param[id] The message id
 * @param[content] The message content
 * @param[embed] One embed
 * @param[embeds] Multiple embeds
 * @param[components] Components for this message
 * @param[file] One file
 * @param[files] Multiple files
 * @param[replace] Whether this should replace the entire message
 *
 * @return[MessageAction]
 */
fun MessageChannel.editMessage(
        id: String,
        content: String? = null,
        embed: MessageEmbed? = null,
        embeds: Embeds? = null,
        components: Components? = null,
        file: NamedFile? = null,
        files: Files? = null,
        replace: Boolean = MessageEditDefaults.replace,
): MessageAction = MessageActionImpl(jda, id, this).apply {
    override(replace)

    content?.let {
        content(it)
    }

    components?.let {
        setActionRows(it.mapNotNull { k -> k as? ActionRow })
    }

    allOf(embed, embeds)?.let {
        setEmbeds(it)
    }

    allOf(file, files).applyIf(true) {
        if (it != null) {
            addFiles(it as Files)
            if (replace)
                retainFilesById(LongRange(0, it.size.toLong()).map(Long::toString))
        }
        else if (replace) {
            retainFilesById(emptyList())
        }
    }
}

/**
 * Edit the message.
 *
 * This makes use of [MessageEditDefaults].
 *
 * This does not currently replace files but may do so in the future.
 *
 * @param[content] The message content
 * @param[embed] One embed
 * @param[embeds] Multiple embeds
 * @param[components] Components for this message
 * @param[file] One file
 * @param[files] Multiple files
 * @param[replace] Whether this should replace the entire message
 *
 * @return[MessageAction]
 */
fun Message.edit(
        content: String? = null,
        embed: MessageEmbed? = null,
        embeds: Embeds? = null,
        components: Components? = null,
        file: NamedFile? = null,
        files: Files? = null,
        replace: Boolean = MessageEditDefaults.replace,
) = channel.editMessage(id, content, embed, embeds, components, file, files, replace).reference(this)
// Defaults for keyword arguments
/**
 * Defaults used for send and reply extensions provided by this module.
 * Each function that relies on these defaults, says so explicitly. This does not apply for edit functions.
 *
 * These can be changed but keep in mind they will apply globally.
 */
object SendDefaults {
    /**
     * The default message content.
     */
    var content: String? = null

    /**
     * The default message embeds
     */
    var embeds: Embeds = emptyList()

    /**
     * The default message components
     */
    var components: Components = emptyList()

    /**
     * The default ephemeral state for interactions
     */
    var ephemeral: Boolean = false
    // Not supporting files since input streams are single use, and I don't think its worth it
}

// And you can also just add these to the individual actions easily

/**
 * Add a collection of [NamedFile] to this request.
 *
 * @param[files] The files to add
 */
fun MessageAction.addFiles(files: Files) = apply {
    files.forEach {
        addFile(it.data, it.name, *it.options)
    }
}

/**
 * Add a collection of [NamedFile] to this request.
 *
 * @param[files] The files to add
 */
fun ReplyCallbackAction.addFiles(files: Files) = apply {
    files.forEach {
        addFile(it.data, it.name, *it.options)
    }
}

/**
 * Add a collection of [NamedFile] to this request.
 *
 * @param[files] The files to add
 */
fun <T> WebhookMessageAction<T>.addFiles(files: Files) = apply {
    files.forEach {
        addFile(it.data, it.name, *it.options)
    }
}

// Using an underscore at the end to prevent overload specialization
// You can remove it with an import alias (import ... as foo) but i would recommend against it

/**
 * Send a reply to this interaction.
 *
 * This makes use of [SendDefaults].
 *
 * @param[content] The message content
 * @param[embed] One embed
 * @param[embeds] Multiple embeds
 * @param[components] Components for this message
 * @param[file] One file
 * @param[files] Multiple files
 * @param[ephemeral] Whether this message is ephemeral
 *
 * @see  [IReplyCallback.deferReply]
 */
fun IReplyCallback.reply_(
        content: String? = SendDefaults.content,
        embed: MessageEmbed? = null,
        embeds: Embeds = SendDefaults.embeds,
        components: Components = SendDefaults.components,
        file: NamedFile? = null,
        files: Files = emptyList(),
        ephemeral: Boolean = SendDefaults.ephemeral,
): ReplyCallbackAction = deferReply().apply {
    setContent(content)
    setEphemeral(ephemeral)

    if (components.isNotEmpty()) {
        addActionRows(components.mapNotNull { it as? ActionRow })
    }

    if (embed != null) {
        addEmbeds(embed)
    }

    if (embeds.isNotEmpty()) {
        addEmbeds(embeds)
    }

    file?.let { addFile(it.data, it.name, *it.options) }
    addFiles(files)
}

/**
 * Send a reply to this interaction.
 *
 * This makes use of [SendDefaults].
 *
 * @param[content] The message content
 * @param[embed] One embed
 * @param[embeds] Multiple embeds
 * @param[components] Components for this message
 * @param[file] One file
 * @param[files] Multiple files
 * @param[ephemeral] Whether this message is ephemeral
 *
 * @return[WebhookMessageAction]
 *
 * @see  [InteractionHook.sendMessage]
 */
fun InteractionHook.send(
        content: String? = SendDefaults.content,
        embed: MessageEmbed? = null,
        embeds: Embeds = SendDefaults.embeds,
        components: Components = SendDefaults.components,
        file: NamedFile? = null,
        files: Files = emptyList(),
        ephemeral: Boolean = SendDefaults.ephemeral,
): WebhookMessageAction<Message> = sendMessage("tmp").apply {
    setContent(content)
    setEphemeral(ephemeral)

    if (components.isNotEmpty()) {
        addActionRows(components.mapNotNull { it as? ActionRow })
    }

    if (embed != null) {
        addEmbeds(embed)
    }

    if (embeds.isNotEmpty()) {
        addEmbeds(embeds)
    }

    file?.let { addFile(it.data, it.name, *it.options) }
    addFiles(files)
}

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
fun MessageChannel.send(
        content: String? = SendDefaults.content,
        embed: MessageEmbed? = null,
        embeds: Embeds = SendDefaults.embeds,
        components: Components = SendDefaults.components,
        file: NamedFile? = null,
        files: Files = emptyList(),
): MessageAction = sendMessage("tmp").apply {
    content(content)

    if (components.isNotEmpty()) {
        setActionRows(components.mapNotNull { it as? ActionRow })
    }

    if (embed != null) {
        setEmbeds(embed)
    }

    if (embeds.isNotEmpty()) {
        setEmbeds(embeds)
    }

    file?.let { addFile(it.data, it.name, *it.options) }
    addFiles(files)
}

/**
 * Send a reply to this message in the same channel.
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
fun Message.reply_(
        content: String? = SendDefaults.content,
        embed: MessageEmbed? = null,
        embeds: Embeds = SendDefaults.embeds,
        components: Components = SendDefaults.components,
        file: NamedFile? = null,
        files: Files = emptyList(),
) = channel.send(content, embed, embeds, components, file, files).reference(this)


val Message.guildOrNull: Guild? get() = try_ { guild }