package net.gloryx.kda

import net.dv8tion.jda.api.entities.Channel
import net.dv8tion.jda.api.entities.channel.IGuildChannelContainer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty

/**
 * Same as [IGuildChannelContainer.getChannelById] but with a generic type parameter instead.
 */
inline fun <reified T : Channel> IGuildChannelContainer.getChannel(id: Long) = getChannelById(T::class.java, id)

/**
 * Same as [IGuildChannelContainer.getChannelById] but with a generic type parameter instead.
 */
inline fun <reified T : Channel> IGuildChannelContainer.getChannel(id: String) = getChannelById(T::class.java, id)

/**
 * Same as [IGuildChannelContainer.getChannelById] but with a generic type parameter instead.
 */
inline fun <reified T : Channel> IGuildChannelContainer.getChannel(id: ULong) = getChannel<T>(id.toLong())

object SLF4J {
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): Logger {
        return LoggerFactory.getLogger(thisRef!!::class.java)!!
    }

    /**
     * Shortcut for [LoggerFactory.getLogger] with string name
     *
     * @param name
     *        The name of the logger
     */
    operator fun invoke(name: String) = lazy {
        LoggerFactory.getLogger(name)!!
    }

    // for some reason you can't link specific overloads...
    /**
     * Shortcut for [LoggerFactory.getLogger] with class parameter
     *
     * @param[T]
     *       The type the logger is for
     */
    inline operator fun <reified T> invoke() = lazy {
        LoggerFactory.getLogger(T::class.java)!!
    }
}
