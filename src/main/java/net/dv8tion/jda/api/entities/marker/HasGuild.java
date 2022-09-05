package net.dv8tion.jda.api.entities.marker;

import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

public interface HasGuild {
    @NotNull
    Guild getGuild();
}
