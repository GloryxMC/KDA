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

package net.dv8tion.jda.internal.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.entities.mixin.channel.middleman.ChannelMixin;
import net.dv8tion.jda.internal.utils.Helpers;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractChannelImpl<T extends AbstractChannelImpl<T>> implements ChannelMixin<T>
{
    protected final long id;
    protected final JDAImpl api;

    protected String name;
    
    public AbstractChannelImpl(long id, JDA api)
    {
        this.id = id;
        this.api = (JDAImpl) api;
    }

    @NotNull
    @Override
    public JDA getJDA()
    {
        return api;
    }

    @Override
    public long getIdLong()
    {
        return id;
    }

    @NotNull
    @Override
    public String getName()
    {
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setName(String name)
    {
        this.name = name;
        return (T) this;
    }

    // -- Union Hooks --

    @NotNull
    public PrivateChannel asPrivateChannel()
    {
        return Helpers.safeChannelCast(this, PrivateChannel.class);
    }

    @NotNull
    public TextChannel asTextChannel()
    {
        return Helpers.safeChannelCast(this, TextChannel.class);
    }

    @NotNull
    public NewsChannel asNewsChannel()
    {
        return Helpers.safeChannelCast(this, NewsChannel.class);
    }

    @NotNull
    public VoiceChannel asVoiceChannel()
    {
        return Helpers.safeChannelCast(this, VoiceChannel.class);
    }

    @NotNull
    public StageChannel asStageChannel()
    {
        return Helpers.safeChannelCast(this, StageChannel.class);
    }

    @NotNull
    public ThreadChannel asThreadChannel()
    {
        return Helpers.safeChannelCast(this, ThreadChannel.class);
    }

    @NotNull
    public Category asCategory()
    {
        return Helpers.safeChannelCast(this, Category.class);
    }

    @NotNull
    public MessageChannel asMessageChannel()
    {
        return Helpers.safeChannelCast(this, MessageChannel.class);
    }

    @NotNull
    public AudioChannel asAudioChannel()
    {
        return Helpers.safeChannelCast(this, AudioChannel.class);
    }

    @NotNull
    public IThreadContainer asThreadContainer()
    {
        return Helpers.safeChannelCast(this, IThreadContainer.class);
    }

    @NotNull
    public GuildChannel asGuildChannel()
    {
        return Helpers.safeChannelCast(this, GuildChannel.class);
    }

    @NotNull
    public GuildMessageChannel asGuildMessageChannel()
    {
        return Helpers.safeChannelCast(this, GuildMessageChannel.class);
    }

    @NotNull
    public StandardGuildChannel asStandardGuildChannel()
    {
        return Helpers.safeChannelCast(this, StandardGuildChannel.class);
    }

    @NotNull
    public StandardGuildMessageChannel asStandardGuildMessageChannel()
    {
        return Helpers.safeChannelCast(this, StandardGuildMessageChannel.class);
    }
}
