package me.devoxin.flight.internal.arguments.types

import net.dv8tion.jda.api.entities.ISnowflake

class Snowflake(val resolved: Long) {
    val jda get() = ISnowflake { resolved }
}
// Exists solely for the snowflake parser.
