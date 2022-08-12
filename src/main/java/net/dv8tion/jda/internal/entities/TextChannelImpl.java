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

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.managers.channel.concrete.TextChannelManager;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.internal.managers.channel.concrete.TextChannelManagerImpl;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TextChannelImpl extends AbstractStandardGuildMessageChannelImpl<TextChannelImpl> implements
        TextChannel,
        DefaultGuildChannelUnion
{
    private int slowmode;

    public TextChannelImpl(long id, GuildImpl guild)
    {
        super(id, guild);
    }

    @NotNull
    @Override
    public ChannelType getType()
    {
        return ChannelType.TEXT;
    }

    @NotNull
    @Override
    public List<Member> getMembers()
    {
        return Collections.unmodifiableList(getGuild().getMembersView().stream()
            .filter(m -> m.hasPermission(this, Permission.VIEW_CHANNEL))
            .collect(Collectors.toList()));
    }

    @Override
    public int getSlowmode()
    {
        return slowmode;
    }

    @NotNull
    @Override
    public ChannelAction<TextChannel> createCopy(@NotNull Guild guild)
    {
        Checks.notNull(guild, "Guild");
        ChannelAction<TextChannel> action = guild.createTextChannel(name).setNSFW(nsfw).setTopic(topic).setSlowmode(slowmode);
        if (guild.equals(getGuild()))
        {
            Category parent = getParentCategory();
            if (parent != null)
                action.setParent(parent);
            for (PermissionOverride o : overrides.valueCollection())
            {
                if (o.isMemberOverride())
                    action.addMemberPermissionOverride(o.getIdLong(), o.getAllowedRaw(), o.getDeniedRaw());
                else
                    action.addRolePermissionOverride(o.getIdLong(), o.getAllowedRaw(), o.getDeniedRaw());
            }
        }
        return action;
    }

    @NotNull
    @Override
    public TextChannelManager getManager()
    {
        return new TextChannelManagerImpl(this);
    }

    public TextChannelImpl setSlowmode(int slowmode)
    {
        this.slowmode = slowmode;
        return this;
    }

    // -- Abstract hooks --
    @Override
    protected void onPositionChange()
    {
        getGuild().getTextChannelsView().clearCachedLists();
    }

    // -- Object overrides --

    @Override
    public String toString()
    {
        return "TC:" + getName() + '(' + id + ')';
    }
}
