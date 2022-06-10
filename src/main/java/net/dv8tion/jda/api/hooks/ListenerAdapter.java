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
package net.dv8tion.jda.api.hooks;

import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.GenericChannelEvent;
import net.dv8tion.jda.api.events.channel.update.*;
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent;
import net.dv8tion.jda.api.events.emote.GenericEmoteEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateRolesEvent;
import net.dv8tion.jda.api.events.emote.update.GenericEmoteUpdateEvent;
import net.dv8tion.jda.api.events.guild.*;
import net.dv8tion.jda.api.events.guild.invite.GenericGuildInviteEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.events.guild.member.update.*;
import net.dv8tion.jda.api.events.guild.override.GenericPermissionOverrideEvent;
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideCreateEvent;
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideDeleteEvent;
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideUpdateEvent;
import net.dv8tion.jda.api.events.guild.update.*;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.events.http.HttpRequestEvent;
import net.dv8tion.jda.api.events.interaction.GenericAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.events.message.react.*;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.*;
import net.dv8tion.jda.api.events.self.*;
import net.dv8tion.jda.api.events.stage.GenericStageInstanceEvent;
import net.dv8tion.jda.api.events.stage.StageInstanceCreateEvent;
import net.dv8tion.jda.api.events.stage.StageInstanceDeleteEvent;
import net.dv8tion.jda.api.events.stage.update.GenericStageInstanceUpdateEvent;
import net.dv8tion.jda.api.events.stage.update.StageInstanceUpdatePrivacyLevelEvent;
import net.dv8tion.jda.api.events.stage.update.StageInstanceUpdateTopicEvent;
import net.dv8tion.jda.api.events.sticker.GenericGuildStickerEvent;
import net.dv8tion.jda.api.events.sticker.GuildStickerAddedEvent;
import net.dv8tion.jda.api.events.sticker.GuildStickerRemovedEvent;
import net.dv8tion.jda.api.events.sticker.update.*;
import net.dv8tion.jda.api.events.thread.GenericThreadEvent;
import net.dv8tion.jda.api.events.thread.ThreadHiddenEvent;
import net.dv8tion.jda.api.events.thread.ThreadRevealedEvent;
import net.dv8tion.jda.api.events.thread.member.GenericThreadMemberEvent;
import net.dv8tion.jda.api.events.thread.member.ThreadMemberJoinEvent;
import net.dv8tion.jda.api.events.thread.member.ThreadMemberLeaveEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.user.UserTypingEvent;
import net.dv8tion.jda.api.events.user.update.*;
import net.dv8tion.jda.internal.utils.ClassWalker;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * An abstract implementation of {@link net.dv8tion.jda.api.hooks.EventListener EventListener} which divides {@link net.dv8tion.jda.api.events.Event Events}
 * for you. You should <b><u>override</u></b> the methods provided by this class for your event listener implementation.
 *
 * <h2>Example:</h2>
 * <pre><code>
 * public class MyReadyListener extends ListenerAdapter
 * {
 *    {@literal @Override}
 *     public void onReady(ReadyEvent event)
 *     {
 *         System.out.println("I am ready to go!");
 *     }
 *
 *    {@literal @Override}
 *     public void onMessageReceived(MessageReceivedEvent event)
 *     {
 *         System.out.printf("[%s]: %s\n", event.getAuthor().getName(), event.getMessage().getContentDisplay());
 *     }
 * }
 * </code></pre>
 *
 * @see net.dv8tion.jda.api.hooks.EventListener EventListener
 * @see net.dv8tion.jda.api.hooks.InterfacedEventManager InterfacedEventManager
 */
public abstract class ListenerAdapter implements EventListener
{
    public void onGenericEvent(@NotNull GenericEvent event) {}
    public void onGenericUpdate(@NotNull UpdateEvent<?, ?> event) {}
    public void onRawGateway(@NotNull RawGatewayEvent event) {}
    public void onGatewayPing(@NotNull GatewayPingEvent event) {}

    //JDA Events
    public void onReady(@NotNull ReadyEvent event) {}
    public void onResumed(@NotNull ResumedEvent event) {}
    public void onReconnected(@NotNull ReconnectedEvent event) {}
    public void onDisconnect(@NotNull DisconnectEvent event) {}
    public void onShutdown(@NotNull ShutdownEvent event) {}
    public void onStatusChange(@NotNull StatusChangeEvent event) {}
    public void onException(@NotNull ExceptionEvent event) {}

    //Interaction Events
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {}
    public void onUserContextInteraction(@NotNull UserContextInteractionEvent event) {}
    public void onMessageContextInteraction(@NotNull MessageContextInteractionEvent event) {}
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {}
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {}
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {}
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {}

    //User Events
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) {}
    public void onUserUpdateDiscriminator(@NotNull UserUpdateDiscriminatorEvent event) {}
    public void onUserUpdateAvatar(@NotNull UserUpdateAvatarEvent event) {}
    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {}
    public void onUserUpdateActivityOrder(@NotNull UserUpdateActivityOrderEvent event) {}
    public void onUserUpdateFlags(@NotNull UserUpdateFlagsEvent event) {}
    public void onUserTyping(@NotNull UserTypingEvent event) {}
    public void onUserActivityStart(@NotNull UserActivityStartEvent event) {}
    public void onUserActivityEnd(@NotNull UserActivityEndEvent event) {}
    public void onUserUpdateActivities(@NotNull UserUpdateActivitiesEvent event) {}

    //Self Events. Fires only in relation to the currently logged in account.
    public void onSelfUpdateAvatar(@NotNull SelfUpdateAvatarEvent event) {}
    public void onSelfUpdateMFA(@NotNull SelfUpdateMFAEvent event) {}
    public void onSelfUpdateName(@NotNull SelfUpdateNameEvent event) {}
    public void onSelfUpdateVerified(@NotNull SelfUpdateVerifiedEvent event) {}

    //Message Events
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {}
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {}
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {}
    public void onMessageBulkDelete(@NotNull MessageBulkDeleteEvent event) {}
    public void onMessageEmbed(@NotNull MessageEmbedEvent event) {}
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {}
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {}
    public void onMessageReactionRemoveAll(@NotNull MessageReactionRemoveAllEvent event) {}
    public void onMessageReactionRemoveEmote(@NotNull MessageReactionRemoveEmoteEvent event) {}

    //PermissionOverride Events
    public void onPermissionOverrideDelete(@NotNull PermissionOverrideDeleteEvent event) {}
    public void onPermissionOverrideUpdate(@NotNull PermissionOverrideUpdateEvent event) {}
    public void onPermissionOverrideCreate(@NotNull PermissionOverrideCreateEvent event) {}

    //StageInstance Event
    public void onStageInstanceDelete(@NotNull StageInstanceDeleteEvent event) {}
    public void onStageInstanceUpdateTopic(@NotNull StageInstanceUpdateTopicEvent event) {}
    public void onStageInstanceUpdatePrivacyLevel(@NotNull StageInstanceUpdatePrivacyLevelEvent event) {}
    public void onStageInstanceCreate(@NotNull StageInstanceCreateEvent event) {}

    //Channel Events
    public void onChannelCreate(@NotNull ChannelCreateEvent event) {}
    public void onChannelDelete(@NotNull ChannelDeleteEvent event) {}

    //Channel Update Events
    public void onChannelUpdateBitrate(@NotNull ChannelUpdateBitrateEvent event) {}
    public void onChannelUpdateName(@NotNull ChannelUpdateNameEvent event) {}
    public void onChannelUpdateNSFW(@NotNull ChannelUpdateNSFWEvent event) {}
    public void onChannelUpdateParent(@NotNull ChannelUpdateParentEvent event) {}
    public void onChannelUpdatePosition(@NotNull ChannelUpdatePositionEvent event) {}
    public void onChannelUpdateRegion(@NotNull ChannelUpdateRegionEvent event) {}
    public void onChannelUpdateSlowmode(@NotNull ChannelUpdateSlowmodeEvent event) {}
    public void onChannelUpdateTopic(@NotNull ChannelUpdateTopicEvent event) {}
    public void onChannelUpdateType(@NotNull ChannelUpdateTypeEvent event) {}
    public void onChannelUpdateUserLimit(@NotNull ChannelUpdateUserLimitEvent event) {}
    public void onChannelUpdateArchived(@NotNull ChannelUpdateArchivedEvent event) {}
    public void onChannelUpdateArchiveTimestamp(@NotNull ChannelUpdateArchiveTimestampEvent event) {}
    public void onChannelUpdateAutoArchiveDuration(@NotNull ChannelUpdateAutoArchiveDurationEvent event) {}
    public void onChannelUpdateLocked(@NotNull ChannelUpdateLockedEvent event) {}
    public void onChannelUpdateInvitable(@NotNull ChannelUpdateInvitableEvent event) {}

    //Thread Events
    public void onThreadRevealed(@NotNull ThreadRevealedEvent event) {}
    public void onThreadHidden(@NotNull ThreadHiddenEvent event) {}

    //Thread Member Events
    public void onThreadMemberJoin(@NotNull ThreadMemberJoinEvent event) {}
    public void onThreadMemberLeave(@NotNull ThreadMemberLeaveEvent event) {}

    //Guild Events
    public void onGuildReady(@NotNull GuildReadyEvent event) {}
    public void onGuildTimeout(@NotNull GuildTimeoutEvent event) {}
    public void onGuildJoin(@NotNull GuildJoinEvent event) {}
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {}
    public void onGuildAvailable(@NotNull GuildAvailableEvent event) {}
    public void onGuildUnavailable(@NotNull GuildUnavailableEvent event) {}
    public void onUnavailableGuildJoined(@NotNull UnavailableGuildJoinedEvent event) {}
    public void onUnavailableGuildLeave(@NotNull UnavailableGuildLeaveEvent event) {}
    public void onGuildBan(@NotNull GuildBanEvent event) {}
    public void onGuildUnban(@NotNull GuildUnbanEvent event) {}
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {}

    //Guild Update Events
    public void onGuildUpdateAfkChannel(@NotNull GuildUpdateAfkChannelEvent event) {}
    public void onGuildUpdateSystemChannel(@NotNull GuildUpdateSystemChannelEvent event) {}
    public void onGuildUpdateRulesChannel(@NotNull GuildUpdateRulesChannelEvent event) {}
    public void onGuildUpdateCommunityUpdatesChannel(@NotNull GuildUpdateCommunityUpdatesChannelEvent event) {}
    public void onGuildUpdateAfkTimeout(@NotNull GuildUpdateAfkTimeoutEvent event) {}
    public void onGuildUpdateExplicitContentLevel(@NotNull GuildUpdateExplicitContentLevelEvent event) {}
    public void onGuildUpdateIcon(@NotNull GuildUpdateIconEvent event) {}
    public void onGuildUpdateMFALevel(@NotNull GuildUpdateMFALevelEvent event) {}
    public void onGuildUpdateName(@NotNull GuildUpdateNameEvent event){}
    public void onGuildUpdateNotificationLevel(@NotNull GuildUpdateNotificationLevelEvent event) {}
    public void onGuildUpdateOwner(@NotNull GuildUpdateOwnerEvent event) {}
    public void onGuildUpdateSplash(@NotNull GuildUpdateSplashEvent event) {}
    public void onGuildUpdateVerificationLevel(@NotNull GuildUpdateVerificationLevelEvent event) {}
    public void onGuildUpdateLocale(@NotNull GuildUpdateLocaleEvent event) {}
    public void onGuildUpdateFeatures(@NotNull GuildUpdateFeaturesEvent event) {}
    public void onGuildUpdateVanityCode(@NotNull GuildUpdateVanityCodeEvent event) {}
    public void onGuildUpdateBanner(@NotNull GuildUpdateBannerEvent event) {}
    public void onGuildUpdateDescription(@NotNull GuildUpdateDescriptionEvent event) {}
    public void onGuildUpdateBoostTier(@NotNull GuildUpdateBoostTierEvent event) {}
    public void onGuildUpdateBoostCount(@NotNull GuildUpdateBoostCountEvent event) {}
    public void onGuildUpdateMaxMembers(@NotNull GuildUpdateMaxMembersEvent event) {}
    public void onGuildUpdateMaxPresences(@NotNull GuildUpdateMaxPresencesEvent event) {}
    public void onGuildUpdateNSFWLevel(@NotNull GuildUpdateNSFWLevelEvent event) {}

    //Guild Invite Events
    public void onGuildInviteCreate(@NotNull GuildInviteCreateEvent event) {}
    public void onGuildInviteDelete(@NotNull GuildInviteDeleteEvent event) {}

    //Guild Member Events
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {}
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {}
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {}

    //Guild Member Update Events
    public void onGuildMemberUpdate(@NotNull GuildMemberUpdateEvent event) {}
    public void onGuildMemberUpdateNickname(@NotNull GuildMemberUpdateNicknameEvent event) {}
    public void onGuildMemberUpdateAvatar(@NotNull GuildMemberUpdateAvatarEvent event) {}
    public void onGuildMemberUpdateBoostTime(@NotNull GuildMemberUpdateBoostTimeEvent event) {}
    public void onGuildMemberUpdatePending(@NotNull GuildMemberUpdatePendingEvent event) {}
    public void onGuildMemberUpdateTimeOut(@NotNull GuildMemberUpdateTimeOutEvent event) {}

    //Guild Voice Events
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {}
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {}
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {}
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {}
    public void onGuildVoiceMute(@NotNull GuildVoiceMuteEvent event) {}
    public void onGuildVoiceDeafen(@NotNull GuildVoiceDeafenEvent event) {}
    public void onGuildVoiceGuildMute(@NotNull GuildVoiceGuildMuteEvent event) {}
    public void onGuildVoiceGuildDeafen(@NotNull GuildVoiceGuildDeafenEvent event) {}
    public void onGuildVoiceSelfMute(@NotNull GuildVoiceSelfMuteEvent event) {}
    public void onGuildVoiceSelfDeafen(@NotNull GuildVoiceSelfDeafenEvent event) {}
    public void onGuildVoiceSuppress(@NotNull GuildVoiceSuppressEvent event) {}
    public void onGuildVoiceStream(@NotNull GuildVoiceStreamEvent event) {}
    public void onGuildVoiceVideo(@NotNull GuildVoiceVideoEvent event) {}
    public void onGuildVoiceRequestToSpeak(@NotNull GuildVoiceRequestToSpeakEvent event) {}

    //Role events
    public void onRoleCreate(@NotNull RoleCreateEvent event) {}
    public void onRoleDelete(@NotNull RoleDeleteEvent event) {}

    //Role Update Events
    public void onRoleUpdateColor(@NotNull RoleUpdateColorEvent event) {}
    public void onRoleUpdateHoisted(@NotNull RoleUpdateHoistedEvent event) {}
    public void onRoleUpdateIcon(@NotNull RoleUpdateIconEvent event) {}
    public void onRoleUpdateMentionable(@NotNull RoleUpdateMentionableEvent event) {}
    public void onRoleUpdateName(@NotNull RoleUpdateNameEvent event) {}
    public void onRoleUpdatePermissions(@NotNull RoleUpdatePermissionsEvent event) {}
    public void onRoleUpdatePosition(@NotNull RoleUpdatePositionEvent event) {}

    //Emote Events
    public void onEmoteAdded(@NotNull EmoteAddedEvent event) {}
    public void onEmoteRemoved(@NotNull EmoteRemovedEvent event) {}

    //Emote Update Events
    public void onEmoteUpdateName(@NotNull EmoteUpdateNameEvent event) {}
    public void onEmoteUpdateRoles(@NotNull EmoteUpdateRolesEvent event) {}

    //Sticker Events
    public void onGuildStickerAdded(@NotNull GuildStickerAddedEvent event) {}
    public void onGuildStickerRemoved(@NotNull GuildStickerRemovedEvent event) {}

    //Sticker Update Events
    public void onGuildStickerUpdateName(@NotNull GuildStickerUpdateNameEvent event) {}
    public void onGuildStickerUpdateTags(@NotNull GuildStickerUpdateTagsEvent event) {}
    public void onGuildStickerUpdateDescription(@NotNull GuildStickerUpdateDescriptionEvent event) {}
    public void onGuildStickerUpdateAvailable(@NotNull GuildStickerUpdateAvailableEvent event) {}

    // Debug Events
    public void onHttpRequest(@NotNull HttpRequestEvent event) {}

    //Generic Events
    public void onGenericInteractionCreate(@NotNull GenericInteractionCreateEvent event) {}
    public void onGenericAutoCompleteInteraction(@NotNull GenericAutoCompleteInteractionEvent event) {}
    public void onGenericComponentInteractionCreate(@NotNull GenericComponentInteractionCreateEvent event) {}
    public void onGenericCommandInteraction(@NotNull GenericCommandInteractionEvent event) {}
    public void onGenericContextInteraction(@NotNull GenericContextInteractionEvent<?> event) {}
    public void onGenericMessage(@NotNull GenericMessageEvent event) {}
    public void onGenericMessageReaction(@NotNull GenericMessageReactionEvent event) {}
    public void onGenericUser(@NotNull GenericUserEvent event) {}
    public void onGenericUserPresence(@NotNull GenericUserPresenceEvent event) {}
    public void onGenericSelfUpdate(@NotNull GenericSelfUpdateEvent event) {}
    public void onGenericStageInstance(@NotNull GenericStageInstanceEvent event) {}
    public void onGenericStageInstanceUpdate(@NotNull GenericStageInstanceUpdateEvent event) {}
    public void onGenericChannel(@NotNull GenericChannelEvent event) {}
    public void onGenericChannelUpdate(@NotNull GenericChannelUpdateEvent<?> event) {}
    public void onGenericThread(@NotNull GenericThreadEvent event) {}
    public void onGenericThreadMember(@NotNull GenericThreadMemberEvent event) {}
    public void onGenericGuild(@NotNull GenericGuildEvent event) {}
    public void onGenericGuildUpdate(@NotNull GenericGuildUpdateEvent event) {}
    public void onGenericGuildInvite(@NotNull GenericGuildInviteEvent event) {}
    public void onGenericGuildMember(@NotNull GenericGuildMemberEvent event) {}
    public void onGenericGuildMemberUpdate(@NotNull GenericGuildMemberUpdateEvent event) {}
    public void onGenericGuildVoice(@NotNull GenericGuildVoiceEvent event) {}
    public void onGenericRole(@NotNull GenericRoleEvent event) {}
    public void onGenericRoleUpdate(@NotNull GenericRoleUpdateEvent event) {}
    public void onGenericEmote(@NotNull GenericEmoteEvent event) {}
    public void onGenericEmoteUpdate(@NotNull GenericEmoteUpdateEvent event) {}
    public void onGenericGuildSticker(@NotNull GenericGuildStickerEvent event) {}
    public void onGenericGuildStickerUpdate(@NotNull GenericGuildStickerUpdateEvent event) {}
    public void onGenericPermissionOverride(@NotNull GenericPermissionOverrideEvent event) {}

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private static final ConcurrentMap<Class<?>, MethodHandle> methods = new ConcurrentHashMap<>();
    private static final Set<Class<?>> unresolved;
    static
    {
        unresolved = ConcurrentHashMap.newKeySet();
        Collections.addAll(unresolved,
            Object.class, // Objects aren't events
            Event.class, // onEvent is final and would never be found
            UpdateEvent.class, // onGenericUpdate has already been called
            GenericEvent.class // onGenericEvent has already been called
        );
    }

    @Override
    public final void onEvent(@NotNull GenericEvent event)
    {
        onGenericEvent(event);
        if (event instanceof UpdateEvent)
            onGenericUpdate((UpdateEvent<?, ?>) event);

        for (Class<?> clazz : ClassWalker.range(event.getClass(), GenericEvent.class))
        {
            if (unresolved.contains(clazz))
                continue;
            MethodHandle mh = methods.computeIfAbsent(clazz, ListenerAdapter::findMethod);
            if (mh == null)
            {
                unresolved.add(clazz);
                continue;
            }

            try
            {
                mh.invoke(this, event);
            }
            catch (Throwable throwable)
            {
                if (throwable instanceof RuntimeException)
                    throw (RuntimeException) throwable;
                if (throwable instanceof Error)
                    throw (Error) throwable;
                throw new IllegalStateException(throwable);
            }
        }
    }

    private static MethodHandle findMethod(Class<?> clazz)
    {
        String name = clazz.getSimpleName();
        MethodType type = MethodType.methodType(Void.TYPE, clazz);
        try
        {
            name = "on" + name.substring(0, name.length() - "Event".length());
            return lookup.findVirtual(ListenerAdapter.class, name, type);
        }
        catch (NoSuchMethodException | IllegalAccessException ignored) {} // this means this is probably a custom event!
        return null;
    }
}
