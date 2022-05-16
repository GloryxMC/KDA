package net.gloryx.kda

import kotlinx.coroutines.CoroutineScope
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.utils.cache.CacheFlag
import net.gloryx.kda.events.CoroutineEventManager
import net.gloryx.kda.events.getDefaultScope
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
