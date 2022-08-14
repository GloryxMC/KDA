package net.gloryx.kda.adventure

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.ComponentSerializer

object DiscordComponentSerializer : ComponentSerializer<Component, Component, String> {
    override fun deserialize(input: String): Component {
        val builder = Component.text()
        return builder.build()
    }

    override fun serialize(component: Component): String {
        val builder = StringBuilder()
        return builder.toString()
    }
}