package net.dv8tion.jda.api.entities;

import net.dv8tion.jda.api.managers.channel.middleman.BaseGuildMessageChannelManager;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.requests.restaction.WebhookAction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;

import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * Represents a standard {@link Guild Guild} {@link MessageChannel MessageChannel}.
 * <br>These are the "<i>normal</i>" message channels that are present in the channel sidebar.
 * These are <b>not</b> {@link ThreadChannel ThreadChannels}.
 *
 * @see GuildMessageChannel
 * @see TextChannel
 * @see NewsChannel
 */
public interface BaseGuildMessageChannel extends GuildMessageChannel, IThreadContainer, GuildChannel, ICategorizableChannel, ICopyableChannel, IPermissionContainer, IMemberContainer, IInviteContainer, IPositionableChannel
{
    //TODO-v5: Docs
    @Override
    @NotNull
    BaseGuildMessageChannelManager<?, ?> getManager();

    /**
     * The topic set for this TextChannel.
     * <br>If no topic has been set, this returns null.
     *
     * @return Possibly-null String containing the topic of this TextChannel.
     */
    @Nullable
    String getTopic();

    /**
     * Whether or not this channel is considered as "NSFW" (Not-Safe-For-Work)
     *
     * @return True, If this TextChannel is considered NSFW by the official Discord Client
     */
    boolean isNSFW();

    /**
     * Retrieves the {@link Webhook Webhooks} attached to this TextChannel.
     *
     * <p>Possible ErrorResponses include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_CHANNEL UNKNOWN_CHANNEL}
     *     <br>if this channel was deleted</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>if we were removed from the guild</li>
     * </ul>
     *
     * @return {@link RestAction} - Type: List{@literal <}{@link Webhook Webhook}{@literal >}
     *         <br>Retrieved an immutable list of Webhooks attached to this channel
     */
    @NotNull
    @CheckReturnValue
    RestAction<List<Webhook>> retrieveWebhooks();

    /**
     * Creates a new {@link Webhook Webhook}.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The webhook could not be created due to a permission discrepancy</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The {@link net.dv8tion.jda.api.Permission#VIEW_CHANNEL VIEW_CHANNEL} permission was removed</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MAX_WEBHOOKS MAX_WEBHOOKS}
     *     <br>If the channel already has reached the maximum capacity for webhooks</li>
     * </ul>
     *
     * @param  name
     *         The default name for the new Webhook.
     *
     * @throws net.dv8tion.jda.api.exceptions.PermissionException
     *         If you do not hold the permission {@link net.dv8tion.jda.api.Permission#MANAGE_WEBHOOKS Manage Webhooks}
     * @throws IllegalArgumentException
     *         If the provided name is {@code null}, blank or not
     *         between 2-100 characters in length
     *
     * @return A specific {@link WebhookAction WebhookAction}
     *         <br>This action allows to set fields for the new webhook before creating it
     */
    @NotNull
    @CheckReturnValue
    WebhookAction createWebhook(@NotNull String name);

    /**
     * Deletes a {@link Webhook Webhook} attached to this channel
     * by the {@code id} specified.
     *
     * <p>Possible ErrorResponses include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK}
     *     <br>The provided id does not refer to a WebHook present in this TextChannel, either due
     *         to it not existing or having already been deleted.</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_CHANNEL UNKNOWN_CHANNEL}
     *     <br>if this channel was deleted</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>if we were removed from the guild</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The send request was attempted after the account lost
     *         {@link net.dv8tion.jda.api.Permission#MANAGE_WEBHOOKS Permission.MANAGE_WEBHOOKS} in the channel.</li>
     * </ul>
     *
     * @param  id
     *         The not-null id for the target Webhook.
     *
     * @throws IllegalArgumentException
     *         If the provided {@code id} is {@code null} or empty.
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the currently logged in account does not have
     *         {@link net.dv8tion.jda.api.Permission#MANAGE_WEBHOOKS Permission.MANAGE_WEBHOOKS} in this channel.
     *
     * @return {@link AuditableRestAction AuditableRestAction}
     */
    @NotNull
    @CheckReturnValue
    AuditableRestAction<Void> deleteWebhookById(@NotNull String id);

    @Override
    @NotNull
    @CheckReturnValue
    ChannelAction<? extends BaseGuildMessageChannel> createCopy(@NotNull Guild guild);

    @Override
    @NotNull
    @CheckReturnValue
    ChannelAction<? extends BaseGuildMessageChannel> createCopy();
}
