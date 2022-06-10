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

package net.dv8tion.jda.api.requests.restaction;

import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

/**
 * Extension of {@link net.dv8tion.jda.api.requests.RestAction RestAction} specifically
 * designed to create a {@link ThreadChannel ThreadChannel}.
 * This extension allows setting properties before executing the action.
 *
 * @see    Message#createThreadChannel(String)
 * @see    IThreadContainer#createThreadChannel(String)
 * @see    IThreadContainer#createThreadChannel(String, boolean)
 * @see    IThreadContainer#createThreadChannel(String, long)
 * @see    IThreadContainer#createThreadChannel(String, String)
 */
public interface ThreadChannelAction extends AuditableRestAction<ThreadChannel>
{
    @NotNull
    @Override
    ThreadChannelAction setCheck(@Nullable BooleanSupplier checks);

    @NotNull
    @Override
    ThreadChannelAction timeout(long timeout, @NotNull TimeUnit unit);

    @NotNull
    @Override
    ThreadChannelAction deadline(long timestamp);

    /**
     * The guild to create this {@link GuildChannel} in
     *
     * @return The guild
     */
    @NotNull
    Guild getGuild();

    /**
     * The {@link ChannelType} for the resulting channel
     *
     * @return The channel type
     */
    @NotNull
    ChannelType getType();

    /**
     * Sets the name for the new GuildChannel
     *
     * @param  name
     *         The not-null name for the new GuildChannel (1-100 chars long)
     *
     * @throws IllegalArgumentException
     *         If the provided name is null or not between 1-100 chars long
     *
     * @return The current ChannelAction, for chaining convenience
     */
    @NotNull
    @CheckReturnValue
    ThreadChannelAction setName(@NotNull String name);

    //TODO-v5: Docs
    @NotNull
    @CheckReturnValue
    ThreadChannelAction setAutoArchiveDuration(@NotNull ThreadChannel.AutoArchiveDuration autoArchiveDuration);

    //TODO-v5: Docs
    @NotNull
    @CheckReturnValue
    ThreadChannelAction setInvitable(boolean isInvitable);
}
