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

import net.dv8tion.jda.api.entities.StageChannel;
import net.dv8tion.jda.api.entities.StageInstance;
import net.dv8tion.jda.api.requests.Request;
import net.dv8tion.jda.api.requests.Response;
import net.dv8tion.jda.api.requests.restaction.StageInstanceAction;
import net.dv8tion.jda.utils.data.DataObject;
import net.dv8tion.jda.internal.entities.GuildImpl;
import net.dv8tion.jda.internal.requests.RestActionImpl;
import net.dv8tion.jda.internal.requests.Route;
import net.dv8tion.jda.internal.utils.Checks;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

public class StageInstanceActionImpl extends RestActionImpl<StageInstance> implements StageInstanceAction
{
    private final StageChannel channel;
    private String topic;
    private StageInstance.PrivacyLevel level = StageInstance.PrivacyLevel.GUILD_ONLY;

    public StageInstanceActionImpl(StageChannel channel)
    {
        super(channel.getJDA(), Route.StageInstances.CREATE_INSTANCE.compile());
        this.channel = channel;
    }

    @NotNull
    @Override
    public StageInstanceAction setCheck(BooleanSupplier checks)
    {
        return (StageInstanceAction) super.setCheck(checks);
    }

    @NotNull
    @Override
    public StageInstanceAction timeout(long timeout, @NotNull TimeUnit unit)
    {
        return (StageInstanceAction) super.timeout(timeout, unit);
    }

    @NotNull
    @Override
    public StageInstanceAction deadline(long timestamp)
    {
        return (StageInstanceAction) super.deadline(timestamp);
    }

    @NotNull
    @Override
    public StageInstanceAction setTopic(@NotNull String topic)
    {
        Checks.notBlank(topic, "Topic");
        Checks.notLonger(topic, 120, "Topic");
        this.topic = topic;
        return this;
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public StageInstanceAction setPrivacyLevel(@NotNull StageInstance.PrivacyLevel level)
    {
        Checks.notNull(level, "PrivacyLevel");
        Checks.check(level != StageInstance.PrivacyLevel.UNKNOWN, "The PrivacyLevel must not be UNKNOWN!");
        Checks.check(level != StageInstance.PrivacyLevel.PUBLIC, "Cannot create PUBLIC stage instances anymore.");
        this.level = level;
        return this;
    }

    @Override
    protected RequestBody finalizeData()
    {
        DataObject body = DataObject.empty();
        body.put("channel_id", channel.getId());
        body.put("topic", topic);
        body.put("privacy_level", level.getKey());
        return getRequestBody(body);
    }

    @Override
    protected void handleSuccess(Response response, Request<StageInstance> request)
    {
        StageInstance instance = api.getEntityBuilder().createStageInstance((GuildImpl) channel.getGuild(), response.getObject());
        request.onSuccess(instance);
    }
}
