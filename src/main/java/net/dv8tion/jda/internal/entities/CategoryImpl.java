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

import gnu.trove.map.TLongObjectMap;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.channel.concrete.CategoryManager;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.requests.restaction.order.CategoryOrderAction;
import net.dv8tion.jda.api.utils.MiscUtil;
import net.dv8tion.jda.internal.entities.mixin.channel.attribute.IPermissionContainerMixin;
import net.dv8tion.jda.internal.entities.mixin.channel.attribute.IPositionableChannelMixin;
import net.dv8tion.jda.internal.managers.channel.concrete.CategoryManagerImpl;
import net.dv8tion.jda.internal.utils.Checks;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import org.jetbrains.annotations.NotNull;

public class CategoryImpl extends AbstractGuildChannelImpl<CategoryImpl> implements
        Category,
        IPositionableChannelMixin<CategoryImpl>,
        IPermissionContainerMixin<CategoryImpl>
{
    private final TLongObjectMap<PermissionOverride> overrides = MiscUtil.newLongMap();

    private int position;

    public CategoryImpl(long id, GuildImpl guild)
    {
        super(id, guild);
    }

    @NotNull
    @Override
    public ChannelType getType()
    {
        return ChannelType.CATEGORY;
    }

    @Override
    public int getPositionRaw()
    {
        return position;
    }

    @NotNull
    @Override
    public ChannelAction<TextChannel> createTextChannel(@NotNull String name)
    {
        ChannelAction<TextChannel> action = getGuild().createTextChannel(name, this);
        return trySync(action);
    }

    @NotNull
    @Override
    public ChannelAction<VoiceChannel> createVoiceChannel(@NotNull String name)
    {
        ChannelAction<VoiceChannel> action = getGuild().createVoiceChannel(name, this);
        return trySync(action);
    }

    @NotNull
    @Override
    public ChannelAction<StageChannel> createStageChannel(@NotNull String name)
    {
        ChannelAction<StageChannel> action = getGuild().createStageChannel(name, this);
        return trySync(action);
    }

    @NotNull
    @Override
    public CategoryOrderAction modifyTextChannelPositions()
    {
        return getGuild().modifyTextChannelPositions(this);
    }

    @NotNull
    @Override
    public CategoryOrderAction modifyVoiceChannelPositions()
    {
        return getGuild().modifyVoiceChannelPositions(this);
    }

    @NotNull
    @Override
    public ChannelAction<Category> createCopy(@NotNull Guild guild)
    {
        Checks.notNull(guild, "Guild");
        ChannelAction<Category> action = guild.createCategory(name);
        if (guild.equals(getGuild()))
        {
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
    public ChannelAction<Category> createCopy()
    {
        return createCopy(getGuild());
    }

    @NotNull
    @Override
    public CategoryManager getManager()
    {
        return new CategoryManagerImpl(this);
    }

    @Override
    public TLongObjectMap<PermissionOverride> getPermissionOverrideMap()
    {
        return overrides;
    }

    @Override
    public CategoryImpl setPosition(int position)
    {
        this.position = position;
        return this;
    }

    @Override
    public String toString()
    {
        return "GC:" + getName() + '(' + id + ')';
    }

    private <T extends GuildChannel> ChannelAction<T> trySync(ChannelAction<T> action)
    {
        Member selfMember = getGuild().getSelfMember();
        if (!selfMember.canSync(this))
        {
            long botPerms = PermissionUtil.getEffectivePermission(this, selfMember);
            for (PermissionOverride override : getPermissionOverrides())
            {
                long perms = override.getDeniedRaw() | override.getAllowedRaw();
                if ((perms & ~botPerms) != 0)
                    return action;
            }
        }
        return action.syncPermissionOverrides();
    }
}
