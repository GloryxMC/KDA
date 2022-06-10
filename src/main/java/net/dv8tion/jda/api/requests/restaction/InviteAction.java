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

import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.MiscUtil;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;

import org.jetbrains.annotations.Nullable;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

/**
 * {@link net.dv8tion.jda.api.entities.Invite Invite} Builder system created as an extension of {@link net.dv8tion.jda.api.requests.RestAction}
 * <br>Provides an easy way to gather and deliver information to Discord to create {@link net.dv8tion.jda.api.entities.Invite Invites}.
 *
 * @see net.dv8tion.jda.api.entities.IInviteContainer#createInvite()
 */
public interface InviteAction extends AuditableRestAction<Invite>
{
    @NotNull
    @Override
    InviteAction setCheck(@Nullable BooleanSupplier checks);

    @NotNull
    @Override
    InviteAction timeout(long timeout, @NotNull TimeUnit unit);

    @NotNull
    @Override
    InviteAction deadline(long timestamp);

    /**
     * Sets the max age in seconds for the invite. Set this to {@code 0} if the invite should never expire. Default is {@code 86400} (24 hours).
     * {@code null} will reset this to the default value.
     *
     * @param  maxAge
     *         The max age for this invite or {@code null} to use the default value.
     *
     * @throws IllegalArgumentException
     *         If maxAge is negative.
     *
     * @return The current InviteAction for chaining.
     */
    @NotNull
    @CheckReturnValue
    InviteAction setMaxAge(@Nullable final Integer maxAge);

    /**
     * Sets the max age for the invite. Set this to {@code 0} if the invite should never expire. Default is {@code 86400} (24 hours).
     * {@code null} will reset this to the default value.
     *
     * @param  maxAge
     *         The max age for this invite or {@code null} to use the default value.
     * @param  timeUnit
     *         The {@link java.util.concurrent.TimeUnit TimeUnit} type of {@code maxAge}.
     *
     * @throws IllegalArgumentException
     *         If maxAge is negative or maxAge is positive and timeUnit is null.
     *
     * @return The current InviteAction for chaining.
     */
    @NotNull
    @CheckReturnValue
    InviteAction setMaxAge(@Nullable final Long maxAge, @NotNull final TimeUnit timeUnit);

    /**
     * Sets the max uses for the invite. Set this to {@code 0} if the invite should have unlimited uses. Default is {@code 0}.
     * {@code null} will reset this to the default value.
     *
     * @param  maxUses
     *         The max uses for this invite or {@code null} to use the default value.
     *
     * @throws IllegalArgumentException
     *         If maxUses is negative.
     *
     * @return The current InviteAction for chaining.
     */
    @NotNull
    @CheckReturnValue
    InviteAction setMaxUses(@Nullable final Integer maxUses);

    /**
     * Sets whether the invite should only grant temporary membership. Default is {@code false}.
     *
     * @param  temporary
     *         Whether the invite should only grant temporary membership or {@code null} to use the default value.
     *
     * @return The current InviteAction for chaining.
     */
    @NotNull
    @CheckReturnValue
    InviteAction setTemporary(@Nullable final Boolean temporary);

    /**
     * Sets whether discord should reuse a similar invite. Default is {@code false}.
     *
     * @param  unique
     *         Whether discord should reuse a similar invite or {@code null} to use the default value.
     *
     * @return The current InviteAction for chaining.
     */
    @NotNull
    @CheckReturnValue
    InviteAction setUnique(@Nullable final Boolean unique);

    /**
     * Sets the id of the targeted application.
     * <br>The invite has to point to a voice channel.
     * The invite will have the {@link Invite.TargetType#EMBEDDED_APPLICATION} target.
     *
     * @param applicationId
     *        The id of the embedded application to target or {@code 0} to remove
     *
     * @return The current InviteAction for chaining.
     */
    @NotNull
    @CheckReturnValue
    InviteAction setTargetApplication(final long applicationId);

    /**
     * Sets the id of the targeted application.
     * <br>The invite has to point to a voice channel.
     * The invite will have the {@link Invite.TargetType#EMBEDDED_APPLICATION} target.
     *
     * @param applicationId
     *        The id of the embedded application to target
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided ID is null
     * @throws java.lang.NumberFormatException
     *         If the provided ID is not a snowflake
     *
     * @return The current InviteAction for chaining.
     */
    @NotNull
    @CheckReturnValue
    default InviteAction setTargetApplication(@NotNull final String applicationId)
    {
        return setTargetApplication(MiscUtil.parseSnowflake(applicationId));
    }

    /**
     * Sets the user whose stream to target for this invite.
     * <br>The user must be streaming in the same channel.
     * The invite will have the {@link Invite.TargetType#STREAM} target.
     *
     * @param userId
     *        The id of the user whose stream to target or {@code 0} to remove.
     *
     * @return The current InviteAction for chaining.
     */
    @NotNull
    @CheckReturnValue
    InviteAction setTargetStream(final long userId);

    /**
     * Sets the user whose stream to display for this invite.
     * <br>The user must be streaming in the same channel.
     * The invite will have the {@link Invite.TargetType#STREAM} target.
     *
     * @param userId
     *        The id of the user whose stream to target.
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided ID is null
     * @throws java.lang.NumberFormatException
     *         If the provided ID is not a snowflake
     *
     * @return The current InviteAction for chaining.
     */
    @NotNull
    @CheckReturnValue
    default InviteAction setTargetStream(@NotNull final String userId)
    {
        return setTargetStream(MiscUtil.parseSnowflake(userId));
    }

    /**
     * Sets the user whose stream to display for this invite.
     * <br>The user must be streaming in the same channel.
     * The invite will have the {@link Invite.TargetType#STREAM} target.
     *
     * @param user
     *        The user whose stream to target.
     *
     * @throws IllegalArgumentException
     *         If the provided user is {@code null}
     *
     * @return The current InviteAction for chaining.
     */
    @NotNull
    @CheckReturnValue
    default InviteAction setTargetStream(@NotNull final User user)
    {
        Checks.notNull(user, "User");
        return setTargetStream(user.getIdLong());
    }

    /**
     * Sets the user whose stream to display for this invite.
     * <br>The user must be streaming in the same channel.
     * The invite will have the {@link Invite.TargetType#STREAM} target.
     *
     * @param member
     *        The member whose stream to target.
     *
     * @throws IllegalArgumentException
     *         If the provided member is {@code null}
     *
     * @return The current InviteAction for chaining.
     */
    @NotNull
    @CheckReturnValue
    default InviteAction setTargetStream(@NotNull final Member member)
    {
        Checks.notNull(member, "Member");
        return setTargetStream(member.getIdLong());
    }

}
