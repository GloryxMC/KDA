package net.gloryx.kda

import net.gloryx.kda.interactions.components.row
import kotlinx.coroutines.CoroutineScope
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.ItemComponent
import net.dv8tion.jda.api.interactions.components.LayoutComponent
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.requests.restaction.MessageAction
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.utils.cache.CacheFlag
import net.gloryx.kda.events.CoroutineEventManager
import net.gloryx.kda.events.getDefaultScope
import java.time.temporal.TemporalAccessor
import kotlin.time.Duration

/**
 * Applies the [CoroutineEventManager] to this builder.
 */
fun JDABuilder.injectKTX(timeout: Duration = Duration.INFINITE) = setEventManager(CoroutineEventManager(timeout=timeout))

/**
 * Applies the [CoroutineEventManager] to this builder.
 */
fun DefaultShardManagerBuilder.injectKTX(timeout: Duration = Duration.INFINITE) = setEventManagerProvider { CoroutineEventManager(timeout=timeout) }

/**
 * The coroutine scope used by the underlying [CoroutineEventManager].
 * If this instance does not use the coroutine event manager, this returns the default scope from [getDefaultScope].
 */
val JDA.scope: CoroutineScope get() = (eventManager as? CoroutineEventManager) ?: getDefaultScope()

/**
 * The coroutine scope used by the underlying [CoroutineEventManager].
 * If this instance does not use the coroutine event manager, this returns the default scope from [getDefaultScope].
 */
val ShardManager.scope: CoroutineScope get() = (shardCache.firstOrNull()?.eventManager as? CoroutineEventManager) ?: getDefaultScope()


/**
 * Convenience method to call [JDABuilder.createLight] and apply a coroutine manager.
 *
 * @param token
 *        The bot token
 * @param enableCoroutines
 *        Whether to set the [CoroutineEventManager] on the JDABuilder instance (Default: true)
 * @param timeout
 *        The timeout [Duration] that an event listener is allowed to run (Default: [Duration.INFINITE] <=> unlimited)
 * @param intent
 *        The gateway intents to use
 * @param intents
 *        The gateway intents to use
 * @param builder
 *        Initialization constructor for the JDABuilder
 *
 * @return [JDA][net.dv8tion.jda.api.JDA] instance returned by [JDABuilder.build]
 */
inline fun light(token: String, enableCoroutines: Boolean = true, timeout: Duration = Duration.INFINITE, intent: GatewayIntent, vararg intents: GatewayIntent, builder: JDABuilder.() -> Unit = {})
        = JDABuilder.createLight(token, intent, *intents)
        .apply(builder)
        .apply {
            if (enableCoroutines)
                injectKTX(timeout=timeout)
        }
        .build()

/**
 * Convenience method to call [JDABuilder.createLight] and apply a coroutine manager.
 *
 * @param token
 *        The bot token
 * @param enableCoroutines
 *        Whether to set the [CoroutineEventManager] on the JDABuilder instance (Default: true)
 * @param timeout
 *        The timeout [Duration] that an event listener is allowed to run (Default: [Duration.INFINITE] <=> unlimited)
 * @param intents
 *        The gateway intents to use
 * @param builder
 *        Initialization constructor for the JDABuilder
 *
 * @return [JDA][net.dv8tion.jda.api.JDA] instance returned by [JDABuilder.build]
 */
inline fun light(token: String, enableCoroutines: Boolean = true, timeout: Duration = Duration.INFINITE, intents: Collection<GatewayIntent>, builder: JDABuilder.() -> Unit = {})
        = JDABuilder.createLight(token, intents)
        .apply(builder)
        .apply {
            if (enableCoroutines)
                injectKTX(timeout=timeout)
        }
        .build()

/**
 * Convenience method to call [JDABuilder.createLight] and apply a coroutine manager.
 * Uses the default intends provided by [JDABuilder.createLight].
 *
 * @param token
 *        The bot token
 * @param enableCoroutines
 *        Whether to set the [CoroutineEventManager] on the JDABuilder instance (Default: true)
 * @param timeout
 *        The timeout [Duration] that an event listener is allowed to run (Default: [Duration.INFINITE] <=> unlimited)
 * @param builder
 *        Initialization constructor for the JDABuilder
 *
 * @return [JDA][net.dv8tion.jda.api.JDA] instance returned by [JDABuilder.build]
 */
inline fun light(token: String, enableCoroutines: Boolean = true, timeout: Duration = Duration.INFINITE, builder: JDABuilder.() -> Unit = {})
        = JDABuilder.createLight(token)
        .apply(builder)
        .apply {
            if (enableCoroutines)
                injectKTX(timeout=timeout)
        }
        .build()




/**
 * Convenience method to call [JDABuilder.createDefault] and apply a coroutine manager.
 *
 * @param token
 *        The bot token
 * @param enableCoroutines
 *        Whether to set the [CoroutineEventManager] on the JDABuilder instance (Default: true)
 * @param timeout
 *        The timeout [Duration] that an event listener is allowed to run (Default: [Duration.INFINITE] <=> unlimited)
 * @param intent
 *        The gateway intents to use
 * @param intents
 *        The gateway intents to use
 * @param builder
 *        Initialization constructor for the JDABuilder
 *
 * @return [JDA][net.dv8tion.jda.api.JDA] instance returned by [JDABuilder.build]
 */
inline fun default(token: String, enableCoroutines: Boolean = true, timeout: Duration = Duration.INFINITE, intent: GatewayIntent, vararg intents: GatewayIntent, builder: JDABuilder.() -> Unit = {})
        = JDABuilder.createDefault(token, intent, *intents)
        .apply(builder)
        .apply {
            if (enableCoroutines)
                injectKTX(timeout=timeout)
        }
        .build()

/**
 * Convenience method to call [JDABuilder.createDefault] and apply a coroutine manager.
 *
 * @param token
 *        The bot token
 * @param enableCoroutines
 *        Whether to set the [CoroutineEventManager] on the JDABuilder instance (Default: true)
 * @param timeout
 *        The timeout [Duration] that an event listener is allowed to run (Default: [Duration.INFINITE] <=> unlimited)
 * @param intents
 *        The gateway intents to use
 * @param builder
 *        Initialization constructor for the JDABuilder
 *
 * @return [JDA][net.dv8tion.jda.api.JDA] instance returned by [JDABuilder.build]
 */
inline fun default(token: String, enableCoroutines: Boolean = true, timeout: Duration = Duration.INFINITE, intents: Collection<GatewayIntent>, builder: JDABuilder.() -> Unit = {})
        = JDABuilder.createDefault(token, intents)
        .apply(builder)
        .apply {
            if (enableCoroutines)
                injectKTX(timeout=timeout)
        }
        .build()

/**
 * Convenience method to call [JDABuilder.createDefault] and apply a coroutine manager.
 * Uses the default intends provided by [JDABuilder.createDefault].
 *
 * @param token
 *        The bot token
 * @param enableCoroutines
 *        Whether to set the [CoroutineEventManager] on the JDABuilder instance (Default: true)
 * @param timeout
 *        The timeout [Duration] that an event listener is allowed to run (Default: [Duration.INFINITE] <=> unlimited)
 * @param builder
 *        Initialization constructor for the JDABuilder
 *
 * @return [JDA][net.dv8tion.jda.api.JDA] instance returned by [JDABuilder.build]
 */
inline fun default(token: String, enableCoroutines: Boolean = true, timeout: Duration = Duration.INFINITE, builder: JDABuilder.() -> Unit = {})
        = JDABuilder.createDefault(token)
        .apply(builder)
        .apply {
            if (enableCoroutines)
                injectKTX(timeout=timeout)
        }
        .build()



/**
 * Convenience method to call [JDABuilder.create] and apply a coroutine manager.
 *
 * @param token
 *        The bot token
 * @param enableCoroutines
 *        Whether to set the [CoroutineEventManager] on the JDABuilder instance (Default: true)
 * @param timeout
 *        The timeout [Duration] that an event listener is allowed to run (Default: [Duration.INFINITE] <=> unlimited)
 * @param intent
 *        The gateway intents to use
 * @param intents
 *        The gateway intents to use
 * @param builder
 *        Initialization constructor for the JDABuilder
 *
 * @return [JDA][net.dv8tion.jda.api.JDA] instance returned by [JDABuilder.build]
 */
inline fun createJDA(token: String, enableCoroutines: Boolean = true, timeout: Duration = Duration.INFINITE, intent: GatewayIntent, vararg intents: GatewayIntent, builder: JDABuilder.() -> Unit = {})
        = JDABuilder.create(token, intent, *intents)
        .apply(builder)
        .apply {
            if (enableCoroutines)
                injectKTX(timeout=timeout)
        }
        .build()

/**
 * Convenience method to call [JDABuilder.create] and apply a coroutine manager.
 *
 * @param token
 *        The bot token
 * @param enableCoroutines
 *        Whether to set the [CoroutineEventManager] on the JDABuilder instance (Default: true)
 * @param timeout
 *        The timeout [Duration] that an event listener is allowed to run (Default: [Duration.INFINITE] <=> unlimited)
 * @param intents
 *        The gateway intents to use
 * @param builder
 *        Initialization constructor for the JDABuilder
 *
 * @return [JDA][net.dv8tion.jda.api.JDA] instance returned by [JDABuilder.build]
 */
inline fun createJDA(token: String, enableCoroutines: Boolean = true, timeout: Duration = Duration.INFINITE, intents: Collection<GatewayIntent>, builder: JDABuilder.() -> Unit = {})
        = JDABuilder.create(token, intents)
        .apply(builder)
        .apply {
            if (enableCoroutines)
                injectKTX(timeout=timeout)
        }
        .build()


/**
 * Can be used to enable or disable intents by using assignment operators.
 * Disabling intents using these operators will also disable the respective cache flags for convenience.
 *
 * ```kt
 * light(token) {
 *   intents += GatewayIntent.GUILD_MEMBERS // enable members intent
 *   intents += listOf(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGES) // enable 2 intents
 *   intents -= GatewayIntent.GUILD_MESSAGE_TYPING // disable typing events
 * }
 * ```
 */
val JDABuilder.intents: IntentAccumulator
    get() = IntentAccumulator(this)

/**
 * Can be used to enable or disable cache flags by using assignment operators.
 *
 * ```kt
 * default(token) {
 *   cache += CacheFlag.VOICE_STATE // enable voice state cache
 *   cache += listOf(CacheFlag.ACTIVITIES, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS) // enable multiple flags
 *   cache -= CacheFlag.ROLE_TAGS // disable role tags cache
 * }
 * ```
 */
val JDABuilder.cache: CacheFlagAccumulator
    get() = CacheFlagAccumulator(this)

class IntentAccumulator(private val builder: JDABuilder) {
    operator fun plusAssign(intents: Collection<GatewayIntent>) {
        builder.enableIntents(intents)
    }

    operator fun plusAssign(intent: GatewayIntent) {
        builder.enableIntents(intent)
    }

    operator fun minusAssign(intents: Collection<GatewayIntent>) {
        builder.disableIntents(intents)

        for (flag in CacheFlag.values()) {
            if (flag.requiredIntent in intents) {
                builder.disableCache(flag)
            }
        }
    }

    operator fun minusAssign(intent: GatewayIntent) {
        builder.disableIntents(intent)

        for (flag in CacheFlag.values()) {
            if (flag.requiredIntent == intent) {
                builder.disableCache(flag)
            }
        }
    }
}

class CacheFlagAccumulator(private val builder: JDABuilder) {
    operator fun plusAssign(cacheFlags: Collection<CacheFlag>) {
        builder.enableCache(cacheFlags)
    }

    operator fun plusAssign(flag: CacheFlag) {
        builder.enableCache(flag)
    }

    operator fun minusAssign(cacheFlags: Collection<CacheFlag>) {
        builder.disableCache(cacheFlags)
    }

    operator fun minusAssign(flag: CacheFlag) {
        builder.disableCache(flag)
    }
}

inline fun Message(
        content: String? = null,
        embed: MessageEmbed? = null,
        embeds: Embeds? = null,
        components: Components? = null,
        nonce: String? = null,
        tts: Boolean = false,
        allowedMentionTypes: Collection<Message.MentionType>? = null,
        mentionUsers: Collection<Long>? = null,
        mentionRoles: Collection<Long>? = null,
        builder: InlineMessage.() -> Unit = {},
): Message {
    return MessageBuilder(content, embed, embeds, components, nonce, tts, allowedMentionTypes, mentionUsers, mentionRoles, builder).build()
}

inline fun Embed(
        description: String? = null,
        title: String? = null,
        url: String? = null,
        color: Int? = null,
        footerText: String? = null,
        footerIcon: String? = null,
        authorName: String? = null,
        authorIcon: String? = null,
        authorUrl: String? = null,
        timestamp: TemporalAccessor? = null,
        image: String? = null,
        thumbnail: String? = null,
        fields: Collection<MessageEmbed.Field> = emptyList(),
        builder: InlineEmbed.() -> Unit = {},
): MessageEmbed {
    return EmbedBuilder(description, title, url, color, footerText, footerIcon,
            authorName, authorIcon, authorUrl, timestamp, image, thumbnail, fields, builder
    ).build()
}

inline fun MessageBuilder(
        content: String? = null,
        embed: MessageEmbed? = null,
        embeds: Embeds? = null,
        components: Components? = null,
        nonce: String? = null,
        tts: Boolean = false,
        allowedMentionTypes: Collection<Message.MentionType>? = null,
        mentionUsers: Collection<Long>? = null,
        mentionRoles: Collection<Long>? = null,
        builder: InlineMessage.() -> Unit = {}
): InlineMessage {
    return MessageBuilder().run {
        setContent(content)
        setNonce(nonce)
        setTTS(tts)
        allowedMentionTypes?.let { setAllowedMentions(it) }
        mentionUsers?.forEach { mentionUsers(it) }
        mentionRoles?.forEach { mentionRoles(it) }

        InlineMessage(this).apply {
            this.embeds += allOf(embed, embeds) ?: emptyList()
            this.components += components ?: emptyList()
            this.builder()
        }
    }
}

inline fun EmbedBuilder(
        description: String? = null,
        title: String? = null,
        url: String? = null,
        color: Int? = null,
        footerText: String? = null,
        footerIcon: String? = null,
        authorName: String? = null,
        authorIcon: String? = null,
        authorUrl: String? = null,
        timestamp: TemporalAccessor? = null,
        image: String? = null,
        thumbnail: String? = null,
        fields: Collection<MessageEmbed.Field> = emptyList(),
        builder: InlineEmbed.() -> Unit = {}
): InlineEmbed {
    return EmbedBuilder().run {
        setDescription(description)
        setTitle(title, url)
        setFooter(footerText, footerIcon)
        setAuthor(authorName, authorUrl, authorIcon)
        setTimestamp(timestamp)
        setThumbnail(thumbnail)
        setImage(image)
        fields.map(this::addField)
        color?.let(this::setColor)
        InlineEmbed(this).apply(builder)
    }
}

class InlineMessage(val builder: MessageBuilder) {
    constructor(message: Message) : this(MessageBuilder(message))

    internal val configuredEmbeds = mutableListOf<MessageEmbed>()
    internal val configuredComponents = mutableListOf<LayoutComponent>()

    fun build() = builder
            .setEmbeds(configuredEmbeds)
            .setActionRows(configuredComponents.mapNotNull { it as? ActionRow })
            .build()

    var content: String? = null
        set(value) {
            builder.setContent(value)
            field = value
        }

    @Deprecated("You should use the embeds property instead, which accepts a collection of embeds", ReplaceWith("embeds"), DeprecationLevel.ERROR)
    var embed: MessageEmbed? = null
        set(value) {
            configuredEmbeds.clear()
            value?.let(configuredEmbeds::add)
            field = value
        }

    val embeds = EmbedAccumulator(this)

    inline fun embed(builder: InlineEmbed.() -> Unit) {
        embeds += EmbedBuilder(description = null).apply(builder).build()
    }

    val components = ComponentAccumulator(this.configuredComponents)

    fun actionRow(vararg components: ItemComponent) {
        this.components += row(*components)
    }

    fun actionRow(components: Collection<ItemComponent>) {
        this.components += components.row()
    }

    var nonce: String? = null
        set(value) {
            builder.setNonce(value)
            field = value
        }

    var tts: Boolean = false
        set(value) {
            builder.setTTS(value)
            field = value
        }

    var allowedMentionTypes = MessageAction.getDefaultMentions()
        set(value) {
            builder.setAllowedMentions(value)
            field = value
        }

    inline fun mentions(build: InlineMentions.() -> Unit) {
        val mentions = InlineMentions().also(build)
        mentions.users.forEach { builder.mentionUsers(it) }
        mentions.roles.forEach { builder.mentionRoles(it) }
    }

    class InlineMentions {
        val users = mutableListOf<Long>()
        val roles = mutableListOf<Long>()

        fun user(user: User) {
            users.add(user.idLong)
        }
        fun user(id: String) {
            users.add(id.toLong())
        }
        fun user(id: Long) {
            users.add(id)
        }

        fun role(role: Role) {
            roles.add(role.idLong)
        }
        fun role(id: String) {
            roles.add(id.toLong())
        }
        fun role(id: Long) {
            roles.add(id)
        }
    }
}

class InlineEmbed(val builder: EmbedBuilder) {
    constructor(embed: MessageEmbed) : this(EmbedBuilder(embed))

    fun build() = builder.build()

    var description: String? = null
        set(value) {
            builder.setDescription(value)
            field = value
        }

    var title: String? = null
        set(value) {
            builder.setTitle(value, url)
            field = value
        }

    var url: String? = null
        set(value) {
            builder.setTitle(title, value)
            field = value
        }

    var color: Int? = null
        set(value) {
            builder.setColor(value ?: Role.DEFAULT_COLOR_RAW)
            field = value
        }

    var timestamp: TemporalAccessor? = null
        set(value) {
            builder.setTimestamp(value)
            field = value
        }

    var image: String? = null
        set(value) {
            builder.setImage(value)
            field = value
        }

    var thumbnail: String? = null
        set(value) {
            builder.setThumbnail(value)
            field = value
        }

    inline fun footer(name: String = "", iconUrl: String? = null, build: InlineFooter.() -> Unit = {}) {
        val footer = InlineFooter(name, iconUrl).apply(build)
        this.builder.setFooter(footer.name, footer.iconUrl)
    }

    inline fun author(name: String? = null, url: String? = null, iconUrl: String? = null, build: InlineAuthor.() -> Unit = {}) {
        val author = InlineAuthor(name, iconUrl, url).apply(build)
        builder.setAuthor(author.name, author.url, author.iconUrl)
    }

    inline fun field(
            name: String = EmbedBuilder.ZERO_WIDTH_SPACE,
            value: String = EmbedBuilder.ZERO_WIDTH_SPACE,
            inline: Boolean = true,
            build: InlineField.() -> Unit = {}
    ) {
        val field = InlineField(name, value, inline).apply(build)
        builder.addField(field.name, field.value, field.inline)
    }

    data class InlineFooter(
            var name: String = "",
            var iconUrl: String? = null
    )

    data class InlineAuthor(
            var name: String? = null,
            var iconUrl: String? = null,
            var url: String? = null
    )

    data class InlineField(
            var name: String = EmbedBuilder.ZERO_WIDTH_SPACE,
            var value: String = EmbedBuilder.ZERO_WIDTH_SPACE,
            var inline: Boolean = true
    )
}

class EmbedAccumulator(private val builder: InlineMessage) {
    operator fun plusAssign(embeds: Collection<MessageEmbed>) {
        builder.configuredEmbeds += embeds
    }

    operator fun plusAssign(embed: MessageEmbed) {
        builder.configuredEmbeds += embed
    }

    operator fun minusAssign(embeds: Collection<MessageEmbed>) {
        builder.configuredEmbeds -= embeds.toSet()
    }

    operator fun minusAssign(embed: MessageEmbed) {
        builder.configuredEmbeds -= embed
    }
}

class ComponentAccumulator(private val config: MutableList<LayoutComponent>) {
    operator fun plusAssign(components: Collection<LayoutComponent>) {
        config += components
    }

    operator fun plusAssign(component: LayoutComponent) {
        config += component
    }

    operator fun minusAssign(components: Collection<LayoutComponent>) {
        config -= components.toSet()
    }

    operator fun minusAssign(component: LayoutComponent) {
        config -= component
    }
}

