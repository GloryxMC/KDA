package net.gloryx.kda

import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty

open class BackedReference<T>(private var entity: T, private val update: (T) -> T?) {
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): T {
        entity = update(entity) ?: entity
        return entity
    }
}

fun User.ref() = BackedReference(this) {
    jda.getUserById(this.idLong)
}

fun Member.ref() = BackedReference(this) {
    guild.getMemberById(idLong)
}

fun Guild.ref() = BackedReference(this) {
    jda.getGuildById(idLong)
}

fun Role.ref() = BackedReference(this) {
    guild.getRoleById(idLong)
}

fun PrivateChannel.ref() = BackedReference(this) {
    jda.getPrivateChannelById(idLong)
}

@Suppress("UNCHECKED_CAST")
fun <T : GuildChannel> T.ref() = BackedReference(this) {
    jda.getGuildChannelById(type, idLong) as T
}

// Example Usage:

/*
class ModLog(channel: TextChannel) {
    private val channel: TextChannel by channel.ref()
    fun onBan(target: User, moderator: Member, reason: String? = null) {
        channel.sendMessage(EmbedBuilder().run {
            setColor(0xa83636)
            setAuthor(moderator.user.asTag, null, moderator.user.avatarUrl)
            setDescription("Banned %#s (%s)".format(target, target.id))
            setTimestamp(Instant.now())
            if (reason != null)
                appendDescription("\n Reason: ").appendDescription(reason)
            build()
        }).queue()
    }
}
class GuildEnvironment(val prefix: String, guild: Guild, modLog: Long) {
    val guild: Guild by guild.ref()
    val modLog = ModLog(guild.getTextChannelById(modLog)!!)
}
 */
