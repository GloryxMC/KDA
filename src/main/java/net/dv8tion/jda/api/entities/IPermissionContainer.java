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

import net.dv8tion.jda.api.managers.channel.attribute.IPermissionContainerManager;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a {@link GuildChannel} that uses {@link PermissionOverride Permission Overrides}.
 *
 * Channels that implement this interface can override permissions for specific users or roles.
 *
 * @see PermissionOverride
 */
public interface IPermissionContainer extends GuildChannel
{
    //TODO-v5: Docs
    @Override
    @NotNull
    IPermissionContainerManager<?, ?> getManager();

    /**
     * The {@link PermissionOverride} relating to the specified {@link Member Member} or {@link Role Role}.
     * If there is no {@link PermissionOverride PermissionOverride} for this {@link GuildChannel GuildChannel}
     * relating to the provided Member or Role, then this returns {@code null}.
     *
     * @param  permissionHolder
     *         The {@link Member Member} or {@link Role Role} whose
     *         {@link PermissionOverride PermissionOverride} is requested.
     *
     * @throws IllegalArgumentException
     *         If the provided permission holder is null, or from a different guild
     *
     * @return Possibly-null {@link PermissionOverride PermissionOverride}
     *         relating to the provided Member or Role.
     */
    @Nullable
    PermissionOverride getPermissionOverride(@NotNull IPermissionHolder permissionHolder);

    /**
     * Gets all of the {@link PermissionOverride PermissionOverrides} that are part
     * of this {@link GuildChannel GuildChannel}.
     * <br>This combines {@link Member Member} and {@link Role Role} overrides.
     * If you would like only {@link Member Member} overrides or only {@link Role Role}
     * overrides, use {@link #getMemberPermissionOverrides()} or {@link #getRolePermissionOverrides()} respectively.
     *
     * <p>This requires {@link net.dv8tion.jda.api.utils.cache.CacheFlag#MEMBER_OVERRIDES CacheFlag.MEMBER_OVERRIDES} to be enabled!
     * Without that CacheFlag, this list will only contain overrides for the currently logged in account and roles.
     *
     * @return Possibly-empty immutable list of all {@link PermissionOverride PermissionOverrides}
     *         for this {@link GuildChannel GuildChannel}.
     */
    @NotNull
    List<PermissionOverride> getPermissionOverrides();

    /**
     * Gets all of the {@link Member Member} {@link PermissionOverride PermissionOverrides}
     * that are part of this {@link GuildChannel GuildChannel}.
     *
     * <p>This requires {@link net.dv8tion.jda.api.utils.cache.CacheFlag#MEMBER_OVERRIDES CacheFlag.MEMBER_OVERRIDES} to be enabled!
     *
     * @return Possibly-empty immutable list of all {@link PermissionOverride PermissionOverrides}
     *         for {@link Member Member}
     *         for this {@link GuildChannel GuildChannel}.
     */
    @NotNull
    default List<PermissionOverride> getMemberPermissionOverrides()
    {
        return Collections.unmodifiableList(getPermissionOverrides().stream()
                .filter(PermissionOverride::isMemberOverride)
                .collect(Collectors.toList()));
    }

    /**
     * Gets all of the {@link Role Role} {@link PermissionOverride PermissionOverrides}
     * that are part of this {@link GuildChannel GuildChannel}.
     *
     * @return Possibly-empty immutable list of all {@link PermissionOverride PermissionOverrides}
     *         for {@link Role Roles}
     *         for this {@link GuildChannel GuildChannel}.
     */
    @NotNull
    default List<PermissionOverride> getRolePermissionOverrides()
    {
        return Collections.unmodifiableList(getPermissionOverrides().stream()
                .filter(PermissionOverride::isRoleOverride)
                .collect(Collectors.toList()));
    }

    /**
     * Creates a new override or updates an existing one.
     * <br>This is similar to calling {@link PermissionOverride#getManager()} if an override exists.
     *
     * @param  permissionHolder
     *         The Member/Role for the override
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If we don't have the permission to {@link net.dv8tion.jda.api.Permission#MANAGE_PERMISSIONS MANAGE_PERMISSIONS}
     * @throws IllegalArgumentException
     *         If the provided permission holder is null or not from this guild
     *
     * @return {@link PermissionOverrideAction}
     *         <br>With the current settings of an existing override or a fresh override with no permissions set
     *
     * @see    PermissionOverrideAction#clear(long)
     * @see    PermissionOverrideAction#grant(long)
     * @see    PermissionOverrideAction#deny(long)
     */
    @NotNull
    @CheckReturnValue
    PermissionOverrideAction upsertPermissionOverride(@NotNull IPermissionHolder permissionHolder);
}
