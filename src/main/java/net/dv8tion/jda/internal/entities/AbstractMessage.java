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
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.sticker.StickerItem;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.ThreadChannelAction;
import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;
import net.dv8tion.jda.internal.utils.Helpers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.OffsetDateTime;
import java.util.*;

public abstract class AbstractMessage implements Message
{
    protected static final String UNSUPPORTED = "This operation is not supported for Messages of this type!";

    protected final String content;
    protected final String nonce;
    protected final boolean isTTS;

    public AbstractMessage(String content, String nonce, boolean isTTS)
    {
        this.content = content;
        this.nonce = nonce;
        this.isTTS = isTTS;
    }

    @NotNull
    @Override
    public String getContentRaw()
    {
        return content;
    }

    @Override
    public String getNonce()
    {
        return nonce;
    }

    @Override
    public boolean isTTS()
    {
        return isTTS;
    }

    protected abstract void unsupported();

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision)
    {
        boolean upper = (flags & FormattableFlags.UPPERCASE) == FormattableFlags.UPPERCASE;
        boolean leftJustified = (flags & FormattableFlags.LEFT_JUSTIFY) == FormattableFlags.LEFT_JUSTIFY;

        String out = content;

        if (upper)
            out = out.toUpperCase(formatter.locale());

        appendFormat(formatter, width, precision, leftJustified, out);
    }

    protected void appendFormat(Formatter formatter, int width, int precision, boolean leftJustified, String out)
    {
        try
        {
            Appendable appendable = formatter.out();
            if (precision > -1 && out.length() > precision)
            {
                appendable.append(Helpers.truncate(out, precision - 3)).append("...");
                return;
            }

            if (leftJustified)
                appendable.append(Helpers.rightPad(out, width));
            else
                appendable.append(Helpers.leftPad(out, width));
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    @Nullable
    @Override
    public MessageReference getMessageReference()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public Mentions getMentions()
    {
        unsupported();
        return null;
    }

    @Override
    public boolean isEdited()
    {
        unsupported();
        return false;
    }

    @Override
    public OffsetDateTime getTimeEdited()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public User getAuthor()
    {
        unsupported();
        return null;
    }

    @Override
    public Member getMember()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public String getJumpUrl()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public String getContentDisplay()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public String getContentStripped()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public List<String> getInvites()
    {
        unsupported();
        return null;
    }

    @Override
    public boolean isFromType(@NotNull ChannelType type)
    {
        unsupported();
        return false;
    }

    @NotNull
    @Override
    public ChannelType getChannelType()
    {
        unsupported();
        return null;
    }

    @Override
    public boolean isWebhookMessage()
    {
        unsupported();
        return false;
    }

    @NotNull
    @Override
    public MessageChannelUnion getChannel()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public GuildMessageChannelUnion getGuildChannel()
    {
        unsupported();
        return null;
    }

    @Override
    public Category getCategory()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public Guild getGuild()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public List<Attachment> getAttachments()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public List<MessageEmbed> getEmbeds()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public List<ActionRow> getActionRows()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public List<MessageReaction> getReactions()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public List<StickerItem> getStickers()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public MessageAction editMessage(@NotNull CharSequence newContent)
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public MessageAction editMessageEmbeds(@NotNull Collection<? extends MessageEmbed> newContent)
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public MessageAction editMessageComponents(@NotNull Collection<? extends LayoutComponent> components)
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public MessageAction editMessageFormat(@NotNull String format, @NotNull Object... args)
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public MessageAction editMessage(@NotNull Message newContent)
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> delete()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public JDA getJDA()
    {
        unsupported();
        return null;
    }

    @Override
    public boolean isPinned()
    {
        unsupported();
        return false;
    }

    @NotNull
    @Override
    public RestAction<Void> pin()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> unpin()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> addReaction(@NotNull Emoji emoji)
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> clearReactions()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> clearReactions(@NotNull Emoji emoji)
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> removeReaction(@NotNull Emoji emoji)
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public RestAction<Void> removeReaction(@NotNull Emoji emoji, @NotNull User user)
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public ReactionPaginationAction retrieveReactionUsers(@NotNull Emoji emoji)
    {
        unsupported();
        return null;
    }

    @Nullable
    @Override
    public MessageReaction getReaction(@NotNull Emoji emoji)
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> suppressEmbeds(boolean suppressed)
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public RestAction<Message> crosspost()
    {
        unsupported();
        return null;
    }

    @Override
    public boolean isSuppressedEmbeds()
    {
        unsupported();
        return false;
    }

    @NotNull
    @Override
    public EnumSet<MessageFlag> getFlags()
    {
        unsupported();
        return null;
    }

    @Override
    public long getFlagsRaw()
    {
        unsupported();
        return 0;
    }

    @Override
    public boolean isEphemeral()
    {
        unsupported();
        return false;
    }

    @Nullable
    @Override
    public ThreadChannel getStartedThread()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public MessageType getType()
    {
        unsupported();
        return null;
    }

    @Nullable
    @Override
    public Message.Interaction getInteraction()
    {
        unsupported();
        return null;
    }

    @Override
    public ThreadChannelAction createThreadChannel(String name)
    {
        unsupported();
        return null;
    }
}
