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
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;

import org.jetbrains.annotations.Nullable;
import java.time.OffsetDateTime;

/**
 * Represents the voice state of a {@link Member Member} in a
 * {@link Guild Guild}.
 *
 * @see Member#getVoiceState()
 */
public interface GuildVoiceState extends ISnowflake
{
    /**
     * Returns the {@link JDA JDA} instance of this VoiceState
     *
     * @return The corresponding JDA instance
     */
    @NotNull
    JDA getJDA();

    /**
     * Returns whether the {@link Member Member} muted themselves.
     *
     * @return The User's self-mute status
     */
    boolean isSelfMuted();

    /**
     * Returns whether the {@link Member Member} deafened themselves.
     *
     * @return The User's self-deaf status
     */
    boolean isSelfDeafened();

    /**
     * Returns whether the {@link Member Member} is muted, either
     * by choice {@link #isSelfMuted()} or muted by an admin {@link #isGuildMuted()}
     *
     * @return the Member's mute status
     */
    boolean isMuted();

    /**
     * Returns whether the {@link Member Member} is deafened, either
     * by choice {@link #isSelfDeafened()} or deafened by an admin {@link #isGuildDeafened()}
     *
     * @return the Member's deaf status
     */
    boolean isDeafened();

    /**
     * Returns whether the {@link Member Member} got muted by an Admin
     *
     * @return the Member's guild-mute status
     */
    boolean isGuildMuted();

    /**
     * Returns whether the {@link Member Member} got deafened by an Admin
     *
     * @return the Member's guild-deaf status
     */
    boolean isGuildDeafened();

    /**
     * Returns true if this {@link Member Member} is unable to speak because the
     * channel is actively suppressing audio communication. This occurs in
     * {@link VoiceChannel VoiceChannels} where the Member either doesn't have
     * {@link net.dv8tion.jda.api.Permission#VOICE_SPEAK Permission#VOICE_SPEAK} or if the channel is the
     * designated AFK channel.
     * <br>This is also used by {@link StageChannel StageChannels} for listeners without speaker approval.
     *
     * @return True, if this {@link Member Member's} audio is being suppressed.
     *
     * @see    #getRequestToSpeakTimestamp()
     */
    boolean isSuppressed();

    /**
     * Returns true if this {@link Member Member} is currently streaming with Go Live.
     *
     * @return True, if this member is streaming
     */
    boolean isStream();

    /**
     * Returns true if this {@link Member Member} has their camera turned on.
     * <br>This does not include streams! See {@link #isStream()}
     *
     * @return True, if this member has their camera turned on.
     */
    boolean isSendingVideo();

    /**
     * Returns the current {@link AudioChannel AudioChannel} that the {@link Member Member}
     * is in. If the {@link Member Member} is currently not in a
     * {@link AudioChannel AudioChannel}, this returns null.
     *
     * @return The AudioChannel that the Member is in, or null.
     */
    @Nullable
    AudioChannel getChannel();

    /**
     * Returns the {@link Guild Guild} for the {@link Member Member}
     * that this {@link GuildVoiceState GuildVoiceState} belongs to. (BackReference)
     *
     * @return the Member's Guild
     */
    @NotNull
    Guild getGuild();

    /**
     * Returns the {@link Member Member} corresponding to this GuildVoiceState instance
     * (BackReference)
     *
     * @return the Member that holds this GuildVoiceState
     */
    @NotNull
    Member getMember();

    /**
     * Used to determine if the {@link Member Member} is currently in an {@link AudioChannel AudioChannel}
     * in the {@link Guild Guild} returned from {@link #getGuild() getGuild()}.<br>
     * If this is {@code false}, {@link #getChannel() getChannel()} will return {@code null}.
     *
     * @return True, if the {@link Member Member} is currently in a {@link AudioChannel AudioChannel}
     *         in this {@link Guild Guild}.
     */
    boolean inAudioChannel();

    /**
     * The Session-Id for this VoiceState
     *
     * @return The Session-Id
     */
    @Nullable
    String getSessionId();

    /**
     * The time at which the user requested to speak.
     * <br>This is used for {@link StageChannel StageChannels} and can only be approved by members with {@link net.dv8tion.jda.api.Permission#VOICE_MUTE_OTHERS Permission.VOICE_MUTE_OTHERS} on the channel.
     *
     * @return The request to speak timestamp, or null if this user didn't request to speak
     */
    @Nullable
    OffsetDateTime getRequestToSpeakTimestamp();

    /**
     * Promote the member to speaker.
     * <p>This requires a non-null {@link #getRequestToSpeakTimestamp()}.
     * You can use {@link #inviteSpeaker()} to invite the member to become a speaker if they haven't requested to speak.
     *
     * <p>This does nothing if the member is not connected to a {@link StageChannel}.
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the currently logged in account does not have {@link net.dv8tion.jda.api.Permission#VOICE_MUTE_OTHERS Permission.VOICE_MUTE_OTHERS}
     *         in the associated {@link StageChannel}
     *
     * @return {@link RestAction}
     */
    @NotNull
    @CheckReturnValue
    RestAction<Void> approveSpeaker();

    /**
     * Reject this members {@link #getRequestToSpeakTimestamp() request to speak}
     * or moves a {@link StageInstance#getSpeakers() speaker} back to the {@link StageInstance#getAudience() audience}.
     * <p>This requires a non-null {@link #getRequestToSpeakTimestamp()}.
     * The member will have to request to speak again.
     *
     * <p>This does nothing if the member is not connected to a {@link StageChannel}.
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the currently logged in account does not have {@link net.dv8tion.jda.api.Permission#VOICE_MUTE_OTHERS Permission.VOICE_MUTE_OTHERS}
     *         in the associated {@link StageChannel}
     *
     * @return {@link RestAction}
     */
    @NotNull
    @CheckReturnValue
    RestAction<Void> declineSpeaker();

    /**
     * Invite this member to become a speaker.
     *
     * <p>This does nothing if the member is not connected to a {@link StageChannel}.
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the currently logged in account does not have {@link net.dv8tion.jda.api.Permission#VOICE_MUTE_OTHERS Permission.VOICE_MUTE_OTHERS}
     *         in the associated {@link StageChannel}
     *
     * @return {@link RestAction}
     */
    @NotNull
    @CheckReturnValue
    RestAction<Void> inviteSpeaker();
}
