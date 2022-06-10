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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.sticker.StickerItem;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.exceptions.MissingAccessException;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import net.dv8tion.jda.utils.data.DataObject;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.requests.CompletedRestAction;
import net.dv8tion.jda.internal.requests.Route;
import net.dv8tion.jda.internal.requests.restaction.AuditableRestActionImpl;
import net.dv8tion.jda.internal.requests.restaction.MessageActionImpl;
import net.dv8tion.jda.internal.utils.Checks;
import net.dv8tion.jda.internal.utils.Helpers;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceivedMessage extends AbstractMessage
{
    private final Object mutex = new Object();

    protected final JDAImpl api;
    protected final long id;
    protected final MessageType type;
    protected final MessageChannel channel;
    protected final MessageReference messageReference;
    protected final boolean fromWebhook;
    protected final boolean pinned;
    protected final User author;
    protected final Member member;
    protected final MessageActivity activity;
    protected final OffsetDateTime editedTime;
    protected final Mentions mentions;
    protected final List<MessageReaction> reactions;
    protected final List<Attachment> attachments;
    protected final List<MessageEmbed> embeds;
    protected final List<StickerItem> stickers;
    protected final List<ActionRow> components;
    protected final int flags;
    protected final Message.Interaction interaction;
    protected final ThreadChannel startedThread;

    protected InteractionHook interactionHook = null; // late-init

    // LAZY EVALUATED
    protected String altContent = null;
    protected String strippedContent = null;

    protected List<String> invites = null;

    public ReceivedMessage(
            long id, MessageChannel channel, MessageType type, MessageReference messageReference,
            boolean fromWebhook, boolean  tts, boolean pinned,
            String content, String nonce, User author, Member member, MessageActivity activity, OffsetDateTime editTime,
            Mentions mentions, List<MessageReaction> reactions, List<Attachment> attachments, List<MessageEmbed> embeds,
            List<StickerItem> stickers, List<ActionRow> components,
            int flags, Message.Interaction interaction, ThreadChannel startedThread)
    {
        super(content, nonce, tts);
        this.id = id;
        this.channel = channel;
        this.messageReference = messageReference;
        this.type = type;
        this.api = (channel != null) ? (JDAImpl) channel.getJDA() : null;
        this.fromWebhook = fromWebhook;
        this.pinned = pinned;
        this.author = author;
        this.member = member;
        this.activity = activity;
        this.editedTime = editTime;
        this.mentions = mentions;
        this.reactions = Collections.unmodifiableList(reactions);
        this.attachments = Collections.unmodifiableList(attachments);
        this.embeds = Collections.unmodifiableList(embeds);
        this.stickers = Collections.unmodifiableList(stickers);
        this.components = Collections.unmodifiableList(components);
        this.flags = flags;
        this.interaction = interaction;
        this.startedThread = startedThread;
    }

    public ReceivedMessage withHook(InteractionHook hook)
    {
        this.interactionHook = hook;
        return this;
    }

    @NotNull
    @Override
    public JDA getJDA()
    {
        return api;
    }

    @Nullable
    @Override
    public MessageReference getMessageReference()
    {
        return messageReference;
    }

    @Override
    public boolean isPinned()
    {
        return pinned;
    }

    @NotNull
    @Override
    public RestAction<Void> pin()
    {
        if (isEphemeral())
            throw new IllegalStateException("Cannot pin ephemeral messages.");
        
        return channel.pinMessageById(getId());
    }

    @NotNull
    @Override
    public RestAction<Void> unpin()
    {
        if (isEphemeral())
            throw new IllegalStateException("Cannot unpin ephemeral messages.");
        
        return channel.unpinMessageById(getId());
    }

    @NotNull
    @Override
    public RestAction<Void> addReaction(@NotNull Emote emote)
    {
        if (isEphemeral())
            throw new IllegalStateException("Cannot add reactions to ephemeral messages.");
        
        Checks.notNull(emote, "Emote");

        boolean missingReaction = reactions.stream()
                   .map(MessageReaction::getReactionEmote)
                   .filter(MessageReaction.ReactionEmote::isEmote)
                   .noneMatch(r -> r.getIdLong() == emote.getIdLong());

        if (missingReaction)
        {
            Checks.check(emote.canInteract(getJDA().getSelfUser(), channel),
                         "Cannot react with the provided emote because it is not available in the current channel.");
        }
        return channel.addReactionById(getId(), emote);
    }

    @NotNull
    @Override
    public RestAction<Void> addReaction(@NotNull String unicode)
    {
        if (isEphemeral())
            throw new IllegalStateException("Cannot add reactions to ephemeral messages.");
        
        return channel.addReactionById(getId(), unicode);
    }

    @NotNull
    @Override
    public RestAction<Void> clearReactions()
    {
        if (isEphemeral())
            throw new IllegalStateException("Cannot clear reactions from ephemeral messages.");
        if (!isFromGuild())
            throw new IllegalStateException("Cannot clear reactions from a message in a Group or PrivateChannel.");
        return getGuildChannel().clearReactionsById(getId());
    }

    @NotNull
    @Override
    public RestAction<Void> clearReactions(@NotNull String unicode)
    {
        if (isEphemeral())
            throw new IllegalStateException("Cannot clear reactions from ephemeral messages.");
        if (!isFromGuild())
            throw new IllegalStateException("Cannot clear reactions from a message in a Group or PrivateChannel.");
        return getGuildChannel().clearReactionsById(getId(), unicode);
    }

    @NotNull
    @Override
    public RestAction<Void> clearReactions(@NotNull Emote emote)
    {
        if (isEphemeral())
            throw new IllegalStateException("Cannot clear reactions from ephemeral messages.");
        if (!isFromGuild())
            throw new IllegalStateException("Cannot clear reactions from a message in a Group or PrivateChannel.");
        return getGuildChannel().clearReactionsById(getId(), emote);
    }

    @NotNull
    @Override
    public RestAction<Void> removeReaction(@NotNull Emote emote)
    {
        if (isEphemeral())
            throw new IllegalStateException("Cannot remove reactions from ephemeral messages.");
        
        return channel.removeReactionById(getId(), emote);
    }

    @NotNull
    @Override
    public RestAction<Void> removeReaction(@NotNull Emote emote, @NotNull User user)
    {
        Checks.notNull(user, "User");  // to prevent NPEs
        if (isEphemeral())
            throw new IllegalStateException("Cannot remove reactions from ephemeral messages.");
        // check if the passed user is the SelfUser, then the ChannelType doesn't matter and
        // we can safely remove that
        if (user.equals(getJDA().getSelfUser()))
            return channel.removeReactionById(getIdLong(), emote);

        if (!isFromGuild())
            throw new IllegalStateException("Cannot remove reactions of others from a message in a Group or PrivateChannel.");
        return getGuildChannel().removeReactionById(getIdLong(), emote, user);
    }

    @NotNull
    @Override
    public RestAction<Void> removeReaction(@NotNull String unicode)
    {
        if (isEphemeral())
            throw new IllegalStateException("Cannot remove reactions from ephemeral messages.");
        
        return channel.removeReactionById(getId(), unicode);
    }

    @NotNull
    @Override
    public RestAction<Void> removeReaction(@NotNull String unicode, @NotNull User user)
    {
        Checks.notNull(user, "User");
        if (user.equals(getJDA().getSelfUser()))
            return channel.removeReactionById(getIdLong(), unicode);

        if (isEphemeral())
            throw new IllegalStateException("Cannot remove reactions from ephemeral messages.");
        if (!isFromGuild())
            throw new IllegalStateException("Cannot remove reactions of others from a message in a Group or PrivateChannel.");
        return getGuildChannel().removeReactionById(getId(), unicode, user);
    }

    @NotNull
    @Override
    public ReactionPaginationAction retrieveReactionUsers(@NotNull Emote emote)
    {
        if (isEphemeral())
            throw new IllegalStateException("Cannot retrieve reactions on ephemeral messages.");
        
        return channel.retrieveReactionUsersById(id, emote);
    }

    @NotNull
    @Override
    public ReactionPaginationAction retrieveReactionUsers(@NotNull String unicode)
    {
        if (isEphemeral())
            throw new IllegalStateException("Cannot retrieve reactions on ephemeral messages.");
        
        return channel.retrieveReactionUsersById(id, unicode);
    }

    @Override
    public MessageReaction getReactionByUnicode(@NotNull String unicode)
    {
        Checks.notEmpty(unicode, "Emoji");
        Checks.noWhitespace(unicode, "Emoji");

        return this.reactions.stream()
            .filter(r -> r.getReactionEmote().isEmoji() && r.getReactionEmote().getEmoji().equals(unicode))
            .findFirst().orElse(null);
    }

    @Override
    public MessageReaction getReactionById(long id)
    {
        return this.reactions.stream()
            .filter(r -> r.getReactionEmote().isEmote() && r.getReactionEmote().getIdLong() == id)
            .findFirst().orElse(null);
    }

    @NotNull
    @Override
    public MessageType getType()
    {
        return type;
    }

    @Nullable
    @Override
    public Interaction getInteraction()
    {
        return interaction;
    }

    @Override
    public long getIdLong()
    {
        return id;
    }

    @NotNull
    @Override
    public String getJumpUrl()
    {
        return Helpers.format(Message.JUMP_URL, isFromGuild() ? getGuild().getId() : "@me", getChannel().getId(), getId());
    }

    @Override
    public boolean isEdited()
    {
        return editedTime != null;
    }

    @Override
    public OffsetDateTime getTimeEdited()
    {
        return editedTime;
    }

    @NotNull
    @Override
    public User getAuthor()
    {
        return author;
    }

    @Override
    public Member getMember()
    {
        return member;
    }

    @NotNull
    @Override
    public String getContentStripped()
    {
        if (strippedContent != null)
            return strippedContent;
        synchronized (mutex)
        {
            if (strippedContent != null)
                return strippedContent;
            return strippedContent = MarkdownSanitizer.sanitize(getContentDisplay());
        }
    }

    @NotNull
    @Override
    public String getContentDisplay()
    {
        if (altContent != null)
            return altContent;

        synchronized (mutex)
        {
            if (altContent != null)
                return altContent;
            String tmp = content;
            for (User user : mentions.getUsers())
            {
                String name;
                if (isFromGuild() && getGuild().isMember(user))
                    name = getGuild().getMember(user).getEffectiveName();
                else
                    name = user.getName();
                tmp = tmp.replaceAll("<@!?" + Pattern.quote(user.getId()) + '>', '@' + Matcher.quoteReplacement(name));
            }
            for (Emote emote : mentions.getEmotes())
            {
                tmp = tmp.replace(emote.getAsMention(), ":" + emote.getName() + ":");
            }
            for (GuildChannel mentionedChannel : mentions.getChannels())
            {
                tmp = tmp.replace(mentionedChannel.getAsMention(), '#' + mentionedChannel.getName());
            }
            for (Role mentionedRole : mentions.getRoles())
            {
                tmp = tmp.replace(mentionedRole.getAsMention(), '@' + mentionedRole.getName());
            }
            return altContent = tmp;
        }
    }

    @NotNull
    @Override
    public String getContentRaw()
    {
        return content;
    }

    @NotNull
    @Override
    public List<String> getInvites()
    {
        if (invites != null)
            return invites;
        synchronized (mutex)
        {
            if (invites != null)
                return invites;
            invites = new ArrayList<>();
            Matcher m = INVITE_PATTERN.matcher(getContentRaw());
            while (m.find())
                invites.add(m.group(1));
            return invites = Collections.unmodifiableList(invites);
        }
    }

    @Override
    public String getNonce()
    {
        return nonce;
    }

    @Override
    public boolean isFromType(@NotNull ChannelType type)
    {
        return getChannelType() == type;
    }

    @NotNull
    @Override
    public ChannelType getChannelType()
    {
        return channel.getType();
    }

    @NotNull
    @Override
    public MessageChannel getChannel()
    {
        return channel;
    }

    @NotNull
    @Override
    public GuildMessageChannel getGuildChannel()
    {
        if (!isFromGuild())
            throw new IllegalStateException("This message was not sent in a guild.");
        return (GuildMessageChannel) channel;
    }

    @NotNull
    @Override
    public PrivateChannel getPrivateChannel()
    {
        if (!isFromType(ChannelType.PRIVATE))
            throw new IllegalStateException("This message was not sent in a private channel");
        return (PrivateChannel) channel;
    }

    @NotNull
    @Override
    public TextChannel getTextChannel()
    {
        if (!isFromType(ChannelType.TEXT))
            throw new IllegalStateException("This message was not sent in a text channel");
        return (TextChannel) channel;
    }

    @NotNull
    @Override
    public NewsChannel getNewsChannel()
    {
        if (!isFromType(ChannelType.NEWS))
            throw new IllegalStateException("This message was not sent in a news channel");
        return (NewsChannel) channel;
    }

    @Override
    public Category getCategory()
    {
        //TODO-v5: Should this actually throw an error here if the GuildMessageChannel doesn't implement ICategorizableChannel?
        GuildMessageChannel chan = getGuildChannel();
        return chan instanceof ICategorizableChannel
            ? ((ICategorizableChannel) chan).getParentCategory()
            : null;
    }

    @NotNull
    @Override
    public Guild getGuild()
    {
        return getGuildChannel().getGuild();
    }

    @NotNull
    @Override
    public List<Attachment> getAttachments()
    {
        return attachments;
    }

    @NotNull
    @Override
    public List<MessageEmbed> getEmbeds()
    {
        return embeds;
    }

    @NotNull
    @Override
    public List<ActionRow> getActionRows()
    {
        return components;
    }

    @NotNull
    @Override
    public Mentions getMentions()
    {
        return mentions;
    }

    @NotNull
    @Override
    public List<MessageReaction> getReactions()
    {
        return reactions;
    }

    @NotNull
    @Override
    public List<StickerItem> getStickers()
    {
        return this.stickers;
    }

    @Override
    public boolean isWebhookMessage()
    {
        return fromWebhook;
    }

    @Override
    public boolean isTTS()
    {
        return isTTS;
    }

    @Nullable
    @Override
    public MessageActivity getActivity()
    {
        return activity;
    }

    @NotNull
    @Override
    public MessageAction editMessage(@NotNull CharSequence newContent)
    {
        checkUser();
        return ((MessageActionImpl) channel.editMessageById(getId(), newContent)).withHook(interactionHook);
    }

    @NotNull
    @Override
    public MessageAction editMessageEmbeds(@NotNull Collection<? extends MessageEmbed> embeds)
    {
        checkUser();
        return ((MessageActionImpl) channel.editMessageEmbedsById(getId(), embeds)).withHook(interactionHook);
    }

    @NotNull
    @Override
    public MessageAction editMessageComponents(@NotNull Collection<? extends LayoutComponent> components)
    {
        checkUser();
        return ((MessageActionImpl) channel.editMessageComponentsById(getId(), components)).withHook(interactionHook);
    }

    @NotNull
    @Override
    public MessageAction editMessageFormat(@NotNull String format, @NotNull Object... args)
    {
        checkUser();
        return ((MessageActionImpl) channel.editMessageFormatById(getId(), format, args)).withHook(interactionHook);
    }

    @NotNull
    @Override
    public MessageAction editMessage(@NotNull Message newContent)
    {
        checkUser();
        return ((MessageActionImpl) channel.editMessageById(getId(), newContent)).withHook(interactionHook);
    }

    private void checkUser()
    {
        if (!getJDA().getSelfUser().equals(getAuthor()))
            throw new IllegalStateException("Attempted to update message that was not sent by this account. You cannot modify other User's messages!");
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> delete()
    {
        if (isEphemeral())
            throw new IllegalStateException("Cannot delete ephemeral messages.");
        
        if (!getJDA().getSelfUser().equals(getAuthor()))
        {
            if (isFromType(ChannelType.PRIVATE))
                throw new IllegalStateException("Cannot delete another User's messages in a PrivateChannel.");

            GuildMessageChannel gChan = getGuildChannel();
            Member sMember = getGuild().getSelfMember();
            if (!sMember.hasAccess(gChan))
                throw new MissingAccessException(gChan, Permission.VIEW_CHANNEL);
            else if (!sMember.hasPermission(gChan, Permission.MESSAGE_MANAGE))
                throw new InsufficientPermissionException(gChan, Permission.MESSAGE_MANAGE);
        }
        if (!type.canDelete())
            throw new IllegalStateException("Cannot delete messages of type " + type);
        return channel.deleteMessageById(getIdLong());
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> suppressEmbeds(boolean suppressed)
    {
        if (isEphemeral())
            throw new IllegalStateException("Cannot suppress embeds on ephemeral messages.");
        
        if (!getJDA().getSelfUser().equals(getAuthor()))
        {
            if (isFromType(ChannelType.PRIVATE))
                throw new PermissionException("Cannot suppress embeds of others in a PrivateChannel.");

            GuildMessageChannel gChan = getGuildChannel();
            if (!getGuild().getSelfMember().hasPermission(gChan, Permission.MESSAGE_MANAGE))
                throw new InsufficientPermissionException(gChan, Permission.MESSAGE_MANAGE);
        }
        JDAImpl jda = (JDAImpl) getJDA();
        Route.CompiledRoute route = Route.Messages.EDIT_MESSAGE.compile(getChannel().getId(), getId());
        int newFlags = flags;
        int suppressionValue = MessageFlag.EMBEDS_SUPPRESSED.getValue();
        if (suppressed)
            newFlags |= suppressionValue;
        else
            newFlags &= ~suppressionValue;
        return new AuditableRestActionImpl<>(jda, route, DataObject.empty().put("flags", newFlags));
    }

    @NotNull
    @Override
    public RestAction<Message> crosspost()
    {
        if (isEphemeral())
            throw new IllegalStateException("Cannot crosspost ephemeral messages.");
        
        if (getFlags().contains(MessageFlag.CROSSPOSTED))
            return new CompletedRestAction<>(getJDA(), this);

        //TODO-v5: Maybe we'll have a `getNewsChannel()` getter that will do this check there?
        if (!(getChannel() instanceof NewsChannel))
            throw new IllegalStateException("This message was not sent in a news channel");

        //TODO-v5: Double check: Is this actually how we crosspost? This, to me, reads as "take the message we just received and crosspost it to the _same exact channel we just received it in_. Makes no sense.
        NewsChannel newsChannel = (NewsChannel) getChannel();
        if (!getGuild().getSelfMember().hasAccess(newsChannel))
            throw new MissingAccessException(newsChannel, Permission.VIEW_CHANNEL);
        if (!getAuthor().equals(getJDA().getSelfUser()) && !getGuild().getSelfMember().hasPermission(newsChannel, Permission.MESSAGE_MANAGE))
            throw new InsufficientPermissionException(newsChannel, Permission.MESSAGE_MANAGE);
        return newsChannel.crosspostMessageById(getId());
    }

    @Override
    public boolean isSuppressedEmbeds()
    {
        return (this.flags & MessageFlag.EMBEDS_SUPPRESSED.getValue()) > 0;
    }

    @NotNull
    @Override
    public EnumSet<MessageFlag> getFlags()
    {
        return MessageFlag.fromBitField(flags);
    }

    @Override
    public long getFlagsRaw()
    {
        return flags;
    }

    @Override
    public boolean isEphemeral()
    {
        return (this.flags & MessageFlag.EPHEMERAL.getValue()) != 0;
    }

    @Nullable
    @Override
    public ThreadChannel getStartedThread()
    {
        return this.startedThread;
    }

    @Override
    public RestAction<ThreadChannel> createThreadChannel(String name)
    {
        return ((IThreadContainer) getGuildChannel()).createThreadChannel(name, this.getIdLong());
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (!(o instanceof ReceivedMessage))
            return false;
        ReceivedMessage oMsg = (ReceivedMessage) o;
        return this.id == oMsg.id;
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(id);
    }

    @Override
    public String toString()
    {
        return author != null
            ? String.format("M:%#s:%.20s(%s)", author, this, getId())
            : String.format("M:%.20s", this); // this message was made using MessageBuilder
    }

    @Override
    protected void unsupported()
    {
        throw new UnsupportedOperationException("This operation is not supported on received messages!");
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision)
    {
        boolean upper = (flags & FormattableFlags.UPPERCASE) == FormattableFlags.UPPERCASE;
        boolean leftJustified = (flags & FormattableFlags.LEFT_JUSTIFY) == FormattableFlags.LEFT_JUSTIFY;
        boolean alt = (flags & FormattableFlags.ALTERNATE) == FormattableFlags.ALTERNATE;

        String out = alt ? getContentRaw() : getContentDisplay();

        if (upper)
            out = out.toUpperCase(formatter.locale());

        appendFormat(formatter, width, precision, leftJustified, out);
    }
}
