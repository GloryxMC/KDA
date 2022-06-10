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

package net.dv8tion.jda.internal.requests.restaction;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.ThreadChannel;
import net.dv8tion.jda.api.requests.Request;
import net.dv8tion.jda.api.requests.Response;
import net.dv8tion.jda.api.requests.restaction.ThreadChannelAction;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.requests.Route;
import net.dv8tion.jda.internal.utils.Checks;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

public class ThreadChannelActionImpl extends AuditableRestActionImpl<ThreadChannel> implements ThreadChannelAction
{
    protected final Guild guild;
    protected final ChannelType type;
    protected final String parentMessageId;

    protected String name;
    protected ThreadChannel.AutoArchiveDuration autoArchiveDuration = null;
    protected Boolean invitable = null;

    public ThreadChannelActionImpl(GuildChannel channel, String name, ChannelType type)
    {
        super(channel.getJDA(), Route.Channels.CREATE_THREAD_WITHOUT_MESSAGE.compile(channel.getId()));
        this.guild = channel.getGuild();
        this.type = type;
        this.parentMessageId = null;

        this.name = name;
    }

    public ThreadChannelActionImpl(GuildChannel channel, String name, String parentMessageId)
    {
        super(channel.getJDA(), Route.Channels.CREATE_THREAD_WITH_MESSAGE.compile(channel.getId(), parentMessageId));
        this.guild = channel.getGuild();
        this.type = channel.getType() == ChannelType.TEXT ? ChannelType.GUILD_PUBLIC_THREAD : ChannelType.GUILD_NEWS_THREAD;
        this.parentMessageId = parentMessageId;

        this.name = name;
    }

    @NotNull
    @Override
    public ThreadChannelActionImpl setCheck(BooleanSupplier checks)
    {
        return (ThreadChannelActionImpl) super.setCheck(checks);
    }

    @NotNull
    @Override
    public ThreadChannelActionImpl timeout(long timeout, @NotNull TimeUnit unit)
    {
        return (ThreadChannelActionImpl) super.timeout(timeout, unit);
    }

    @NotNull
    @Override
    public ThreadChannelActionImpl deadline(long timestamp)
    {
        return (ThreadChannelActionImpl) super.deadline(timestamp);
    }

    @NotNull
    @Override
    public Guild getGuild()
    {
        return guild;
    }

    @NotNull
    @Override
    public ChannelType getType()
    {
        return type;
    }

    @NotNull
    @Override
    @CheckReturnValue
    public ThreadChannelActionImpl setName(@NotNull String name)
    {
        Checks.notEmpty(name, "Name");
        Checks.notLonger(name, 100, "Name");
        this.name = name;
        return this;
    }

    @NotNull
    @Override
    public ThreadChannelAction setAutoArchiveDuration(@NotNull ThreadChannel.AutoArchiveDuration autoArchiveDuration)
    {
        Checks.notNull(autoArchiveDuration, "autoArchiveDuration");
        this.autoArchiveDuration = autoArchiveDuration;
        return this;
    }

    @NotNull
    @Override
    public ThreadChannelAction setInvitable(boolean invitable)
    {
        if (type != ChannelType.GUILD_PRIVATE_THREAD)
            throw new UnsupportedOperationException("Can only set invitable on private threads");

        this.invitable = invitable;
        return this;
    }

    @Override
    protected RequestBody finalizeData()
    {
        DataObject object = DataObject.empty();

        object.put("name", name);

        //The type is selected by discord itself if we are using a parent message, so don't send it.
        if (parentMessageId == null)
            object.put("type", type.getId());

        if (autoArchiveDuration != null)
            object.put("auto_archive_duration", autoArchiveDuration.getMinutes());
        if (invitable != null)
            object.put("invitable", invitable);

        return getRequestBody(object);
    }

    @Override
    protected void handleSuccess(Response response, Request<ThreadChannel> request)
    {
        ThreadChannel channel = api.getEntityBuilder().createThreadChannel(response.getObject(), guild.getIdLong());
        request.onSuccess(channel);
    }
}
