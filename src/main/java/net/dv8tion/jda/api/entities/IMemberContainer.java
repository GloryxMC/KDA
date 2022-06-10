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

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a {@link GuildChannel} that is capable of containing members.
 *
 * Implementations interpret this meaning as best applies to them:
 *
 * For example,
 * <ul>
 *   <li>{@link TextChannel TextChannels} implement this as the {@link Member members} that have {@link net.dv8tion.jda.api.Permission#VIEW_CHANNEL}</li>
 *   <li>{@link VoiceChannel VoiceChannels} implement this as what {@link Member members} are currently connected to the channel.</li>
 * </ul>
 *
 *
 * @see IMemberContainer#getMembers()
 */
public interface IMemberContainer extends GuildChannel
{
    /**
     * A List of all {@link Member Members} that are in this GuildChannel
     * <br>For {@link TextChannel TextChannels},
     * this returns all Members with the {@link net.dv8tion.jda.api.Permission#VIEW_CHANNEL Permission.VIEW_CHANNEL} Permission.
     * <br>For {@link VoiceChannel VoiceChannels},
     * this returns all Members that joined that VoiceChannel.
     * <br>For {@link Category Categories},
     * this returns all Members who are in its child channels.
     *
     * @return An immutable List of {@link Member Members} that are in this GuildChannel.
     */
    @NotNull
    List<Member> getMembers();
}
