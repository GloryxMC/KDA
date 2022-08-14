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

package net.dv8tion.jda.internal.entities.mixin.channel.middleman;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.pagination.MessagePaginationAction;
import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;
import net.dv8tion.jda.api.utils.AttachmentOption;
import net.dv8tion.jda.api.utils.TimeUtil;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.requests.RestActionImpl;
import net.dv8tion.jda.internal.requests.Route;
import net.gloryx.kda.markdown.component.EmbedComponent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public interface MessageChannelMixin<T extends MessageChannelMixin<T>> extends
        MessageChannel,
        MessageChannelUnion
{
    // ---- Default implementations of interface ----
    @NotNull
    default List<CompletableFuture<Void>> purgeMessages(@NotNull List<? extends Message> messages)
    {
        if (messages == null || messages.isEmpty())
            return Collections.emptyList();

        if (!canDeleteOtherUsersMessages())
        {
            for (Message m : messages)
            {
                if (m.getAuthor().equals(getJDA().getSelfUser()))
                    continue;

                if (getType() == ChannelType.PRIVATE)
                    throw new IllegalStateException("Cannot delete messages of other users in a private channel");
                else
                    throw new InsufficientPermissionException((GuildChannel) this, Permission.MESSAGE_MANAGE, "Cannot delete messages of other users");
            }
        }

        return MessageChannelUnion.super.purgeMessages(messages);
    }

    @NotNull
    default List<CompletableFuture<Void>> purgeMessagesById(@NotNull long... messageIds)
    {
        if (messageIds == null || messageIds.length == 0)
            return Collections.emptyList();

        //If we can't use the bulk delete system, then use the standard purge defined in MessageChannel
        if (!canDeleteOtherUsersMessages())
            return MessageChannelUnion.super.purgeMessagesById(messageIds);

        // remove duplicates and sort messages
        List<CompletableFuture<Void>> list = new LinkedList<>();
        TreeSet<Long> bulk = new TreeSet<>(Comparator.reverseOrder());
        TreeSet<Long> norm = new TreeSet<>(Comparator.reverseOrder());
        long twoWeeksAgo = TimeUtil.getDiscordTimestamp(System.currentTimeMillis() - (14 * 24 * 60 * 60 * 1000) + 10000);
        for (long messageId : messageIds)
        {
            if (messageId > twoWeeksAgo) //Bulk delete cannot delete messages older than 2 weeks.
                bulk.add(messageId);
            else
                norm.add(messageId);
        }

        // delete chunks of 100 messages each
        if (!bulk.isEmpty())
        {
            List<String> toDelete = new ArrayList<>(100);
            while (!bulk.isEmpty())
            {
                toDelete.clear();
                for (int i = 0; i < 100 && !bulk.isEmpty(); i++)
                    toDelete.add(Long.toUnsignedString(bulk.pollLast()));

                //If we only had 1 in the bulk collection then use the standard deleteMessageById request
                // as you cannot bulk delete a single message
                if (toDelete.size() == 1)
                    list.add(deleteMessageById(toDelete.get(0)).submit());
                else if (!toDelete.isEmpty())
                    list.add(bulkDeleteMessages(toDelete).submit());
            }
        }

        // delete messages too old for bulk delete
        if (!norm.isEmpty())
        {
            for (long message : norm)
                list.add(deleteMessageById(message).submit());
        }
        return list;
    }

    @NotNull
    @CheckReturnValue
    default MessageAction sendMessage(@NotNull CharSequence text)
    {
        checkCanAccessChannel();
        checkCanSendMessage();
        return MessageChannelUnion.super.sendMessage(text);
    }

    @NotNull
    @CheckReturnValue
    default MessageAction sendMessageEmbeds(@NotNull EmbedComponent embed, @NotNull EmbedComponent... other)
    {
        checkCanAccessChannel();
        checkCanSendMessage();
        checkCanSendMessageEmbeds();
        return MessageChannelUnion.super.sendMessageEmbeds(embed, other);
    }

    @NotNull
    @CheckReturnValue
    default MessageAction sendMessageEmbeds(@NotNull Collection<? extends EmbedComponent> embeds)
    {
        checkCanAccessChannel();
        checkCanSendMessage();
        checkCanSendMessageEmbeds();
        return MessageChannelUnion.super.sendMessageEmbeds(embeds);
    }

    @NotNull
    @CheckReturnValue
    default MessageAction sendMessage(@NotNull Message msg)
    {
        checkCanAccessChannel();
        checkCanSendMessage();
        return MessageChannelUnion.super.sendMessage(msg);
    }

    @NotNull
    @CheckReturnValue
    default MessageAction sendFile(@NotNull InputStream data, @NotNull String fileName, @NotNull AttachmentOption... options)
    {
        checkCanAccessChannel();
        checkCanSendMessage();
        checkCanSendFiles();
        return MessageChannelUnion.super.sendFile(data, fileName, options);
    }

    @NotNull
    @CheckReturnValue
    default RestAction<Message> retrieveMessageById(@NotNull String messageId)
    {
        checkCanAccessChannel();
        checkCanViewHistory();
        return MessageChannelUnion.super.retrieveMessageById(messageId);
    }

    @NotNull
    @CheckReturnValue
    default AuditableRestAction<Void> deleteMessageById(@NotNull String messageId)
    {
       checkCanAccessChannel();
       //We don't know if this is a Message sent by us or another user, so we can't run checks for Permission.MESSAGE_MANAGE
       return MessageChannelUnion.super.deleteMessageById(messageId);
    }

    @NotNull
    @Override
    default MessageHistory getHistory()
    {
        checkCanAccessChannel();
        checkCanViewHistory();
        return MessageChannelUnion.super.getHistory();
    }

    @NotNull
    @CheckReturnValue
    default MessagePaginationAction getIterableHistory()
    {
        checkCanAccessChannel();
        checkCanViewHistory();
        return MessageChannelUnion.super.getIterableHistory();
    }

    @NotNull
    @CheckReturnValue
    default MessageHistory.MessageRetrieveAction getHistoryAround(@NotNull String messageId, int limit)
    {
        checkCanAccessChannel();
        checkCanViewHistory();
        return MessageChannelUnion.super.getHistoryAround(messageId, limit);
    }

    @NotNull
    @CheckReturnValue
    default MessageHistory.MessageRetrieveAction getHistoryAfter(@NotNull String messageId, int limit)
    {
        checkCanAccessChannel();
        checkCanViewHistory();
        return MessageChannelUnion.super.getHistoryAfter(messageId, limit);
    }

    @NotNull
    @CheckReturnValue
    default MessageHistory.MessageRetrieveAction getHistoryBefore(@NotNull String messageId, int limit)
    {
        checkCanAccessChannel();
        checkCanViewHistory();
        return MessageChannelUnion.super.getHistoryBefore(messageId, limit);
    }

    @NotNull
    @CheckReturnValue
    default MessageHistory.MessageRetrieveAction getHistoryFromBeginning(int limit)
    {
        checkCanAccessChannel();
        checkCanViewHistory();
        return MessageHistory.getHistoryFromBeginning(this).limit(limit);
    }

    @NotNull
    @CheckReturnValue
    default RestAction<Void> sendTyping()
    {
        checkCanAccessChannel();
        return MessageChannelUnion.super.sendTyping();
    }

    @NotNull
    @CheckReturnValue
    default RestAction<Void> addReactionById(@NotNull String messageId, @NotNull Emoji emoji)
    {
        checkCanAccessChannel();
        checkCanAddReactions();
        return MessageChannelUnion.super.addReactionById(messageId, emoji);
    }

    @NotNull
    @CheckReturnValue
    default RestAction<Void> removeReactionById(@NotNull String messageId, @NotNull Emoji emoji)
    {
        checkCanAccessChannel();
        checkCanRemoveReactions();
        return MessageChannelUnion.super.removeReactionById(messageId, emoji);
    }

    @NotNull
    @CheckReturnValue
    default ReactionPaginationAction retrieveReactionUsersById(@NotNull String messageId, @NotNull Emoji emoji)
    {
        checkCanAccessChannel();
        checkCanRemoveReactions();
        return MessageChannelUnion.super.retrieveReactionUsersById(messageId, emoji);
    }

    @NotNull
    @CheckReturnValue
    default RestAction<Void> pinMessageById(@NotNull String messageId)
    {
        checkCanAccessChannel();
        checkCanControlMessagePins();
        return MessageChannelUnion.super.pinMessageById(messageId);
    }

    @NotNull
    @CheckReturnValue
    default RestAction<Void> unpinMessageById(@NotNull String messageId)
    {
        checkCanAccessChannel();
        checkCanControlMessagePins();
        return MessageChannelUnion.super.unpinMessageById(messageId);
    }

    @NotNull
    @CheckReturnValue
    default RestAction<List<Message>> retrievePinnedMessages()
    {
        checkCanAccessChannel();
        return MessageChannelUnion.super.retrievePinnedMessages();
    }

    @NotNull
    @CheckReturnValue
    default MessageAction editMessageById(@NotNull String messageId, @NotNull CharSequence newContent)
    {
        checkCanAccessChannel();
        checkCanSendMessage();
        return MessageChannelUnion.super.editMessageById(messageId, newContent);
    }

    @NotNull
    @CheckReturnValue
    default MessageAction editMessageById(@NotNull String messageId, @NotNull Message newContent)
    {
       checkCanAccessChannel();
       checkCanSendMessage();
       return MessageChannelUnion.super.editMessageById(messageId, newContent);
    }


    @NotNull
    @CheckReturnValue
    default MessageAction editMessageEmbedsById(@NotNull String messageId, @NotNull Collection<? extends EmbedComponent> newEmbeds)
    {
        checkCanAccessChannel();
        checkCanSendMessage();
        checkCanSendMessageEmbeds();
        return MessageChannelUnion.super.editMessageEmbedsById(messageId, newEmbeds);
    }

    @NotNull
    @CheckReturnValue
    default MessageAction editMessageComponentsById(@NotNull String messageId, @NotNull Collection<? extends LayoutComponent> components)
    {
        checkCanAccessChannel();
        checkCanSendMessage();
        return MessageChannelUnion.super.editMessageComponentsById(messageId, components);
    }

    // ---- State Accessors ----
    T setLatestMessageIdLong(long latestMessageId);

    // ---- Mixin Hooks ----
    void checkCanAccessChannel();
    void checkCanSendMessage();
    void checkCanSendMessageEmbeds();
    void checkCanSendFiles();
    void checkCanViewHistory();
    void checkCanAddReactions();
    void checkCanRemoveReactions();
    void checkCanControlMessagePins();

    boolean canDeleteOtherUsersMessages();

    // ---- Helpers -----
    default RestActionImpl<Void> bulkDeleteMessages(Collection<String> messageIds)
    {
        DataObject body = DataObject.empty().put("messages", messageIds);
        Route.CompiledRoute route = Route.Messages.DELETE_MESSAGES.compile(getId());
        return new RestActionImpl<>(getJDA(), route, body);
    }
}
