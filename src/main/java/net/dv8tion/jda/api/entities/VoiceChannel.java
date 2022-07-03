/*
 * Copyright 2015 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.dv8tion.jda.api.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.managers.channel.concrete.VoiceChannelManager;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Discord Voice GuildChannel.
 * <br>Adds additional information specific to voice channels in Discord.
 *
 * @see GuildChannel
 * @see TextChannel
 * @see Category
 *
 * @see   Guild#getVoiceChannelCache()
 * @see   Guild#getVoiceChannels()
 * @see   Guild#getVoiceChannelsByName(String, boolean)
 * @see   Guild#getVoiceChannelById(long)
 *
 * @see   JDA#getVoiceChannelCache()
 * @see   JDA#getVoiceChannels()
 * @see   JDA#getVoiceChannelsByName(String, boolean)
 * @see   JDA#getVoiceChannelById(long)
 */
public interface VoiceChannel extends AudioChannel, StandardGuildChannel
{
    /**
     * The maximum amount of {@link Member Members} that can be in this
     * {@link VoiceChannel VoiceChannel} at once.
     * <br>0 - No limit
     *
     * @return The maximum amount of members allowed in this channel at once.
     */
    int getUserLimit();

    @NotNull
    @Override
    ChannelAction<VoiceChannel> createCopy(@NotNull Guild guild);

    @NotNull
    @Override
    default ChannelAction<VoiceChannel> createCopy()
    {
        return createCopy(getGuild());
    }

    @NotNull
    @Override
    VoiceChannelManager getManager();
}
