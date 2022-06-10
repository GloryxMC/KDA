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

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.sticker.StickerItem;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SystemMessage extends ReceivedMessage
{
    public SystemMessage(
            long id, MessageChannel channel, MessageType type, MessageReference messageReference,
            boolean fromWebhook, boolean  tts, boolean pinned,
            String content, String nonce, User author, Member member, MessageActivity activity, OffsetDateTime editTime,
            Mentions mentions, List<MessageReaction> reactions, List<Attachment> attachments, List<MessageEmbed> embeds,
            List<StickerItem> stickers, int flags, ThreadChannel startedThread)
    {
        super(id, channel, type, messageReference, fromWebhook, tts, pinned, content, nonce, author, member,
                activity, editTime, mentions, reactions, attachments, embeds, stickers, Collections.emptyList(), flags, null, startedThread);
    }

    @NotNull
    @Override
    public RestAction<Void> pin()
    {
        throw new UnsupportedOperationException("Cannot pin message of this Message Type. MessageType: " + getType());
    }

    @NotNull
    @Override
    public RestAction<Void> unpin()
    {
        throw new UnsupportedOperationException("Cannot unpin message of this Message Type. MessageType: " + getType());
    }

    @NotNull
    @Override
    public MessageAction editMessage(@NotNull CharSequence newContent)
    {
        throw new UnsupportedOperationException("Cannot edit message of this Message Type. MessageType: " + getType());
    }

    @NotNull
    @Override
    public MessageAction editMessageEmbeds(@NotNull Collection<? extends MessageEmbed> embeds)
    {
        throw new UnsupportedOperationException("Cannot edit message of this Message Type. MessageType: " + getType());
    }

    @NotNull
    @Override
    public MessageAction editMessageComponents(@NotNull Collection<? extends LayoutComponent> components)
    {
        throw new UnsupportedOperationException("Cannot edit message of this Message Type. MessageType: " + getType());
    }

    @NotNull
    @Override
    public MessageAction editMessageFormat(@NotNull String format, @NotNull Object... args)
    {
        throw new UnsupportedOperationException("Cannot edit message of this Message Type. MessageType: " + getType());
    }

    @NotNull
    @Override
    public MessageAction editMessage(@NotNull Message newContent)
    {
        throw new UnsupportedOperationException("Cannot edit message of this Message Type. MessageType: " + getType());
    }

    @Override
    public String toString()
    {
        return "M:[" + type + ']' + author + '(' + id + ')';
    }
}
