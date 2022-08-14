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

import net.dv8tion.jda.api.interactions.callbacks.IDeferrableCallback;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageUpdateAction;
import net.dv8tion.jda.api.utils.AttachmentOption;
import net.dv8tion.jda.internal.utils.Checks;
import net.gloryx.kda.markdown.component.EmbedComponent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;
import java.io.*;
import java.util.*;

/**
 * Interface which allows sending messages through the webhooks API.
 * <br>Interactions can use these through {@link IDeferrableCallback#getHook()}.
 *
 * @see Webhook
 * @see net.dv8tion.jda.api.interactions.InteractionHook
 */
public interface WebhookClient<T>
{
    /**
     * Send a message to this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     * </ul>
     *
     * @param  content
     *         The message content
     *
     * @throws IllegalArgumentException
     *         If the content is null, empty, or longer than {@link Message#MAX_CONTENT_LENGTH}
     *
     * @return {@link WebhookMessageAction}
     */
    @NotNull
    @CheckReturnValue
    WebhookMessageAction<T> sendMessage(@NotNull String content);

    /**
     * Send a message to this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     * </ul>
     *
     * @param  message
     *         The message to send
     *
     * @throws IllegalArgumentException
     *         If the message is null
     *
     * @return {@link WebhookMessageAction}
     */
    @NotNull
    @CheckReturnValue
    WebhookMessageAction<T> sendMessage(@NotNull Message message);

    /**
     * Send a message to this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     * </ul>
     *
     * @param  format
     *         Format string for the message content
     * @param  args
     *         Format arguments for the content
     *
     * @throws IllegalArgumentException
     *         If the format string is null or the resulting content is longer than {@link Message#MAX_CONTENT_LENGTH}
     *
     * @return {@link WebhookMessageAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageAction<T> sendMessageFormat(@NotNull String format, @NotNull Object... args)
    {
        Checks.notNull(format, "Format String");
        return sendMessage(String.format(format, args));
    }

    /**
     * Send a message to this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     * </ul>
     *
     * @param  embeds
     *         {@link EmbedComponent MessageEmbeds} to use (up to {@value Message#MAX_EMBED_COUNT} in total)
     *
     * @throws IllegalArgumentException
     *         If any of the embeds are null, more than {@value Message#MAX_EMBED_COUNT}, or longer than {@link EmbedComponent#EMBED_MAX_LENGTH_BOT}.
     *
     * @return {@link WebhookMessageAction}
     */
    @NotNull
    @CheckReturnValue
    WebhookMessageAction<T> sendMessageEmbeds(@NotNull Collection<? extends EmbedComponent> embeds);

    /**
     * Send a message to this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     * </ul>
     *
     * @param  embed
     *         {@link EmbedComponent} to use
     * @param  embeds
     *         Additional {@link EmbedComponent MessageEmbeds} to use (up to {@value Message#MAX_EMBED_COUNT} in total)
     *
     * @throws IllegalArgumentException
     *         If any of the embeds are null, more than {@value Message#MAX_EMBED_COUNT}, or longer than {@link EmbedComponent#EMBED_MAX_LENGTH_BOT}.
     *
     * @return {@link WebhookMessageAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageAction<T> sendMessageEmbeds(@NotNull EmbedComponent embed, @NotNull EmbedComponent... embeds)
    {
        Checks.notNull(embed, "MessageEmbeds");
        Checks.noneNull(embeds, "MessageEmbeds");
        List<EmbedComponent> embedList = new ArrayList<>();
        embedList.add(embed);
        Collections.addAll(embedList, embeds);
        return sendMessageEmbeds(embedList);
    }

    /**
     * Send a message to this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p><b>Uploading images with Embeds</b>
     * <br>When uploading an <u>image</u> you can reference said image using the specified filename as URI {@code attachment://filename.ext}.
     *
     * <p><u>Example</u>
     * <pre><code>
     * WebhookClient hook; // = reference of a WebhookClient such as interaction.getHook()
     * EmbedBuilder embed = new EmbedBuilder();
     * InputStream file = new FileInputStream("image.png"); // the name in your file system can be different from the name used in discord
     * embed.setImage("attachment://cat.png") // we specify this in sendFile as "cat.png"
     *      .setDescription("This is a cute cat :3");
     * hook.sendFile(file, "cat.png").addEmbeds(embed.build()).queue();
     * </code></pre>
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#REQUEST_ENTITY_TOO_LARGE REQUEST_ENTITY_TOO_LARGE}
     *     <br>The file exceeds the maximum upload size of {@link Message#MAX_FILE_SIZE}</li>
     * </ul>
     *
     * @param  data
     *         The InputStream data to upload to the webhook.
     * @param  name
     *         The file name that should be sent to discord
     *         <br>Refer to the documentation for {@link #sendFile(File, String, AttachmentOption...)} for information about this parameter.
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws IllegalArgumentException
     *         If the provided file or filename is {@code null} or {@code empty}.
     *
     * @return {@link WebhookMessageAction}
     */
    @NotNull
    @CheckReturnValue
    WebhookMessageAction<T> sendFile(@NotNull InputStream data, @NotNull String name, @NotNull AttachmentOption... options);

    /**
     * Send a message to this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>This is a shortcut to {@link #sendFile(File, String, AttachmentOption...)} by way of using {@link File#getName()}.
     * <pre>sendFile(file, file.getName())</pre>
     *
     * <p><b>Uploading images with Embeds</b>
     * <br>When uploading an <u>image</u> you can reference said image using the specified filename as URI {@code attachment://filename.ext}.
     *
     * <p><u>Example</u>
     * <pre><code>
     * WebhookClient hook; // = reference of a WebhookClient such as interaction.getHook()
     * EmbedBuilder embed = new EmbedBuilder();
     * File data = new File("image.png"); // the name in your file system can be different from the name used in discord
     * embed.setImage("attachment://cat.png") // we specify this in sendFile as "cat.png"
     *      .setDescription("This is a cute cat :3");
     * hook.sendFile(file, "cat.png").addEmbeds(embed.build()).queue();
     * </code></pre>
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#REQUEST_ENTITY_TOO_LARGE REQUEST_ENTITY_TOO_LARGE}
     *     <br>The file exceeds the maximum upload size of {@link Message#MAX_FILE_SIZE}</li>
     * </ul>
     *
     * @param  file
     *         The {@link File} data to upload to the webhook.
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws IllegalArgumentException
     *         If the provided file is {@code null}.
     *
     * @return {@link WebhookMessageAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageAction<T> sendFile(@NotNull File file, @NotNull AttachmentOption... options)
    {
        Checks.notNull(file, "File");
        return sendFile(file, file.getName(), options);
    }

    /**
     * Send a message to this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>The {@code name} parameter is used to inform Discord about what the file should be called. This is 2 fold:
     * <ol>
     *     <li>The file name provided is the name that is found in {@link Message.Attachment#getFileName()}
     *          after upload and it is the name that will show up in the client when the upload is displayed.
     *     <br>Note: The fileName does not show up on the Desktop client for images. It does on mobile however.</li>
     *     <li>The extension of the provided fileName also determines how Discord will treat the file. Discord currently only
     *         has special handling for image file types, but the fileName's extension must indicate that it is an image file.
     *         This means it has to end in something like .png, .jpg, .jpeg, .gif, etc. As a note, you can also not provide
     *         a full name for the file and instead ONLY provide the extension like "png" or "gif" and Discord will generate
     *         a name for the upload and append the fileName as the extension.</li>
     * </ol>
     *
     * <p><b>Uploading images with Embeds</b>
     * <br>When uploading an <u>image</u> you can reference said image using the specified filename as URI {@code attachment://filename.ext}.
     *
     * <p><u>Example</u>
     * <pre><code>
     * WebhookClient hook; // = reference of a WebhookClient such as interaction.getHook()
     * EmbedBuilder embed = new EmbedBuilder();
     * byte[] data = IOUtils.readAllBytes(new FileInputStream("image.png")); // the name in your file system can be different from the name used in discord
     * embed.setImage("attachment://cat.png") // we specify this in sendFile as "cat.png"
     *      .setDescription("This is a cute cat :3");
     * hook.sendFile(file, "cat.png").addEmbeds(embed.build()).queue();
     * </code></pre>
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#REQUEST_ENTITY_TOO_LARGE REQUEST_ENTITY_TOO_LARGE}
     *     <br>The file exceeds the maximum upload size of {@link Message#MAX_FILE_SIZE}</li>
     * </ul>
     *
     * @param  file
     *         The {@link File} data to upload to the webhook.
     * @param  name
     *         The file name that should be sent to discord
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws IllegalArgumentException
     *         If the provided file or filename is {@code null} or {@code empty}.
     *
     * @return {@link WebhookMessageAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageAction<T> sendFile(@NotNull File file, @NotNull String name, @NotNull AttachmentOption... options)
    {
        Checks.notNull(file, "File");
        Checks.check(file.exists() && file.canRead(),
                "Provided file doesn't exist or cannot be read!");
        Checks.notNull(name, "Name");

        try
        {
            return sendFile(new FileInputStream(file), name, options);
        }
        catch (FileNotFoundException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Send a message to this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p><b>Uploading images with Embeds</b>
     * <br>When uploading an <u>image</u> you can reference said image using the specified filename as URI {@code attachment://filename.ext}.
     *
     * <p><u>Example</u>
     * <pre><code>
     * WebhookClient hook; // = reference of a WebhookClient such as interaction.getHook()
     * EmbedBuilder embed = new EmbedBuilder();
     * byte[] data = IOUtils.readAllBytes(new FileInputStream("image.png")); // the name in your file system can be different from the name used in discord
     * embed.setImage("attachment://cat.png") // we specify this in sendFile as "cat.png"
     *      .setDescription("This is a cute cat :3");
     * hook.sendFile(file, "cat.png").addEmbeds(embed.build()).queue();
     * </code></pre>
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#REQUEST_ENTITY_TOO_LARGE REQUEST_ENTITY_TOO_LARGE}
     *     <br>The file exceeds the maximum upload size of {@link Message#MAX_FILE_SIZE}</li>
     * </ul>
     *
     * @param  data
     *         The {@code byte[]} data to upload to the webhook.
     * @param  name
     *         The file name that should be sent to discord
     *         <br>Refer to the documentation for {@link #sendFile(File, String, AttachmentOption...)} for information about this parameter.
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws IllegalArgumentException
     *         If the provided file or filename is {@code null} or {@code empty}.
     *
     * @return {@link WebhookMessageAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageAction<T> sendFile(@NotNull byte[] data, @NotNull String name, @NotNull AttachmentOption... options)
    {
        Checks.notNull(data, "Data");
        Checks.notNull(name, "Name");
        return sendFile(new ByteArrayInputStream(data), name, options);
    }

    /**
     * Edit an existing message sent by this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  content
     *         The new message content to use
     *
     * @throws IllegalArgumentException
     *         If the provided content is null, empty, or longer than {@link Message#MAX_CONTENT_LENGTH}
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    WebhookMessageUpdateAction<T> editMessageById(@NotNull String messageId, @NotNull String content);

    /**
     * Edit an existing message sent by this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  content
     *         The new message content to use
     *
     * @throws IllegalArgumentException
     *         If the provided content is null, empty, or longer than {@link Message#MAX_CONTENT_LENGTH}
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageById(long messageId, @NotNull String content)
    {
        return editMessageById(Long.toUnsignedString(messageId), content);
    }

    /**
     * Edit an existing message sent by this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  message
     *         The new message to replace the existing message with
     *
     * @throws IllegalArgumentException
     *         If the provided message is null
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    WebhookMessageUpdateAction<T> editMessageById(@NotNull String messageId, @NotNull Message message);

    /**
     * Edit an existing message sent by this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  message
     *         The new message to replace the existing message with
     *
     * @throws IllegalArgumentException
     *         If the provided message is null
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageById(long messageId, Message message)
    {
        return editMessageById(Long.toUnsignedString(messageId), message);
    }

    /**
     * Edit an existing message sent by this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  format
     *         Format string for the message content
     * @param  args
     *         Format arguments for the content
     *
     * @throws IllegalArgumentException
     *         If the formatted string is null, empty, or longer than {@link Message#MAX_CONTENT_LENGTH}
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageFormatById(@NotNull String messageId, @NotNull String format, @NotNull Object... args)
    {
        Checks.notNull(format, "Format String");
        return editMessageById(messageId, String.format(format, args));
    }

    /**
     * Edit an existing message sent by this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  format
     *         Format string for the message content
     * @param  args
     *         Format arguments for the content
     *
     * @throws IllegalArgumentException
     *         If the formatted string is null, empty, or longer than {@link Message#MAX_CONTENT_LENGTH}
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageFormatById(long messageId, @NotNull String format, @NotNull Object... args)
    {
        return editMessageFormatById(Long.toUnsignedString(messageId), format, args);
    }

    /**
     * Edit an existing message sent by this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  embeds
     *         {@link EmbedComponent MessageEmbeds} to use (up to {@value Message#MAX_EMBED_COUNT} in total)
     *
     * @throws IllegalArgumentException
     *         If the provided embeds are null, or more than {@value Message#MAX_EMBED_COUNT}
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    WebhookMessageUpdateAction<T> editMessageEmbedsById(@NotNull String messageId, @NotNull Collection<? extends EmbedComponent> embeds);

    /**
     * Edit an existing message sent by this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  embeds
     *         {@link EmbedComponent MessageEmbeds} to use (up to {@value Message#MAX_EMBED_COUNT} in total)
     *
     * @throws IllegalArgumentException
     *         If the provided embeds are null, or more than {@value Message#MAX_EMBED_COUNT}
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageEmbedsById(long messageId, @NotNull Collection<? extends EmbedComponent> embeds)
    {
        return editMessageEmbedsById(Long.toUnsignedString(messageId), embeds);
    }

    /**
     * Edit an existing message sent by this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  embeds
     *         The new {@link EmbedComponent MessageEmbeds} to use
     *
     * @throws IllegalArgumentException
     *         If the provided embeds are null, or more than 10
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageEmbedsById(@NotNull String messageId, @NotNull EmbedComponent... embeds)
    {
        Checks.noneNull(embeds, "MessageEmbeds");
        return editMessageEmbedsById(messageId, Arrays.asList(embeds));
    }

    /**
     * Edit an existing message sent by this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  embeds
     *         The new {@link EmbedComponent MessageEmbeds} to use
     *
     * @throws IllegalArgumentException
     *         If the provided embeds are null, or more than 10
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageEmbedsById(long messageId, @NotNull EmbedComponent... embeds)
    {
        return editMessageEmbedsById(Long.toUnsignedString(messageId), embeds);
    }

    /**
     * Edit an existing message sent by this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  components
     *         The new component layouts for this message, such as {@link ActionRow ActionRows}
     *
     * @throws IllegalArgumentException
     *         If the provided components are null, or more than 5 layouts are provided
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    WebhookMessageUpdateAction<T> editMessageComponentsById(@NotNull String messageId, @NotNull Collection<? extends LayoutComponent> components); // We use LayoutComponent for forward compatibility here

    /**
     * Edit an existing message sent by this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  components
     *         The new component layouts for this message, such as {@link ActionRow ActionRows}
     *
     * @throws IllegalArgumentException
     *         If the provided components are null, or more than 5 layouts are provided
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageComponentsById(long messageId, @NotNull Collection<? extends LayoutComponent> components)
    {
        return editMessageComponentsById(Long.toUnsignedString(messageId), components);
    }

    /**
     * Edit an existing message sent by this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  components
     *         The new component layouts for this message, such as {@link ActionRow ActionRows}
     *
     * @throws IllegalArgumentException
     *         If the provided components are null, or more than 5 layouts are provided
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageComponentsById(@NotNull String messageId, @NotNull LayoutComponent... components)
    {
        Checks.noneNull(components, "LayoutComponents");
        return editMessageComponentsById(messageId, Arrays.asList(components));
    }

    /**
     * Edit an existing message sent by this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  components
     *         The new component layouts for this message, such as {@link ActionRow ActionRows}
     *
     * @throws IllegalArgumentException
     *         If the provided components are null, or more than 5 layouts are provided
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageComponentsById(long messageId, @NotNull LayoutComponent... components)
    {
        return editMessageComponentsById(Long.toUnsignedString(messageId), components);
    }


    /**
     * Edit an existing message sent by this webhook.
     * <br>The provided file will be appended to the message. You cannot delete or edit existing files on a message.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p><b>Uploading images with Embeds</b>
     * <br>When uploading an <u>image</u> you can reference said image using the specified filename as URI {@code attachment://filename.ext}.
     *
     * <p><u>Example</u>
     * <pre><code>
     * WebhookClient hook; // = reference of a WebhookClient such as interaction.getHook()
     * EmbedBuilder embed = new EmbedBuilder();
     * InputStream file = new FileInputStream("image.png"); // the name in your file system can be different from the name used in discord
     * embed.setImage("attachment://cat.png") // we specify this in sendFile as "cat.png"
     *      .setDescription("This is a cute cat :3");
     * hook.editMessageById(messageId, file, "cat.png").setEmbeds(embed.build()).queue();
     * </code></pre>
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  data
     *         The InputStream data to upload to the webhook.
     * @param  name
     *         The file name that should be sent to discord
     *         <br>Refer to the documentation for {@link #sendFile(File, String, AttachmentOption...)} for information about this parameter.
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws IllegalArgumentException
     *         If the provided message id, data, or filename is {@code null}.
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    WebhookMessageUpdateAction<T> editMessageById(@NotNull String messageId, @NotNull InputStream data, @NotNull String name, @NotNull AttachmentOption... options);

    /**
     * Edit an existing message sent by this webhook.
     * <br>The provided file will be appended to the message. You cannot delete or edit existing files on a message.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>This is a shortcut to {@link #editMessageById(String, File, String, AttachmentOption...)} by way of using {@link File#getName()}.
     * <pre>editMessageById(messageId, file, file.getName())</pre>
     *
     * <p><b>Uploading images with Embeds</b>
     * <br>When uploading an <u>image</u> you can reference said image using the specified filename as URI {@code attachment://filename.ext}.
     *
     * <p><u>Example</u>
     * <pre><code>
     * WebhookClient hook; // = reference of a WebhookClient such as interaction.getHook()
     * EmbedBuilder embed = new EmbedBuilder();
     * File file = new File("image.png"); // the name in your file system can be different from the name used in discord
     * embed.setImage("attachment://cat.png") // we specify this in sendFile as "cat.png"
     *      .setDescription("This is a cute cat :3");
     * hook.editMessageById(messageId, file, "cat.png").setEmbeds(embed.build()).queue();
     * </code></pre>
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  file
     *         The {@link File} data to upload to the webhook.
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws IllegalArgumentException
     *         If the provided message id or file is {@code null}.
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageById(@NotNull String messageId, @NotNull File file, @NotNull AttachmentOption... options)
    {
        Checks.notNull(file, "File");
        return editMessageById(messageId, file, file.getName(), options);
    }

    /**
     * Edit an existing message sent by this webhook.
     * <br>The provided file will be appended to the message. You cannot delete or edit existing files on a message.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p><b>Uploading images with Embeds</b>
     * <br>When uploading an <u>image</u> you can reference said image using the specified filename as URI {@code attachment://filename.ext}.
     *
     * <p><u>Example</u>
     * <pre><code>
     * WebhookClient hook; // = reference of a WebhookClient such as interaction.getHook()
     * EmbedBuilder embed = new EmbedBuilder();
     * File file = new File("image.png"); // the name in your file system can be different from the name used in discord
     * embed.setImage("attachment://cat.png") // we specify this in sendFile as "cat.png"
     *      .setDescription("This is a cute cat :3");
     * hook.editMessageById(messageId, file, "cat.png").setEmbeds(embed.build()).queue();
     * </code></pre>
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  file
     *         The {@link File} data to upload to the webhook.
     * @param  name
     *         The file name that should be sent to discord
     *         <br>Refer to the documentation for {@link #sendFile(File, String, AttachmentOption...)} for information about this parameter.
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws IllegalArgumentException
     *         If the provided file, message id, or filename is {@code null}.
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageById(@NotNull String messageId, @NotNull File file, @NotNull String name, @NotNull AttachmentOption... options)
    {
        Checks.notNull(file, "File");
        Checks.check(file.exists() && file.canRead(),
                "Provided file doesn't exist or cannot be read!");
        Checks.notNull(name, "Name");

        try
        {
            return editMessageById(messageId, new FileInputStream(file), name, options);
        }
        catch (FileNotFoundException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Edit an existing message sent by this webhook.
     * <br>The provided file will be appended to the message. You cannot delete or edit existing files on a message.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p><b>Uploading images with Embeds</b>
     * <br>When uploading an <u>image</u> you can reference said image using the specified filename as URI {@code attachment://filename.ext}.
     *
     * <p><u>Example</u>
     * <pre><code>
     * WebhookClient hook; // = reference of a WebhookClient such as interaction.getHook()
     * EmbedBuilder embed = new EmbedBuilder();
     * InputStream file = new FileInputStream("image.png"); // the name in your file system can be different from the name used in discord
     * embed.setImage("attachment://cat.png") // we specify this in sendFile as "cat.png"
     *      .setDescription("This is a cute cat :3");
     * hook.editMessageById(messageId, file, "cat.png").setEmbeds(embed.build()).queue();
     * </code></pre>
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  data
     *         The InputStream data to upload to the webhook.
     * @param  name
     *         The file name that should be sent to discord
     *         <br>Refer to the documentation for {@link #sendFile(File, String, AttachmentOption...)} for information about this parameter.
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws IllegalArgumentException
     *         If the provided message id, data, or filename is {@code null}.
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageById(@NotNull String messageId, @NotNull byte[] data, @NotNull String name, @NotNull AttachmentOption... options)
    {
        Checks.notNull(data, "Data");
        Checks.notNull(name, "Name");

        return editMessageById(messageId, new ByteArrayInputStream(data), name, options);
    }

    /**
     * Edit an existing message sent by this webhook.
     * <br>The provided file will be appended to the message. You cannot delete or edit existing files on a message.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p><b>Uploading images with Embeds</b>
     * <br>When uploading an <u>image</u> you can reference said image using the specified filename as URI {@code attachment://filename.ext}.
     *
     * <p><u>Example</u>
     * <pre><code>
     * WebhookClient hook; // = reference of a WebhookClient such as interaction.getHook()
     * EmbedBuilder embed = new EmbedBuilder();
     * InputStream file = new FileInputStream("image.png"); // the name in your file system can be different from the name used in discord
     * embed.setImage("attachment://cat.png") // we specify this in sendFile as "cat.png"
     *      .setDescription("This is a cute cat :3");
     * hook.editMessageById(messageId, file, "cat.png").setEmbeds(embed.build()).queue();
     * </code></pre>
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  data
     *         The InputStream data to upload to the webhook.
     * @param  name
     *         The file name that should be sent to discord
     *         <br>Refer to the documentation for {@link #sendFile(File, String, AttachmentOption...)} for information about this parameter.
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws IllegalArgumentException
     *         If the provided data or filename is {@code null}.
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageById(long messageId, @NotNull InputStream data, @NotNull String name, @NotNull AttachmentOption... options)
    {
        return editMessageById(Long.toUnsignedString(messageId), data, name, options);
    }

    /**
     * Edit an existing message sent by this webhook.
     * <br>The provided file will be appended to the message. You cannot delete or edit existing files on a message.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>This is a shortcut to {@link #sendFile(File, String, AttachmentOption...)} by way of using {@link File#getName()}.
     * <pre>sendFile(file, file.getName())</pre>
     *
     * <p><b>Uploading images with Embeds</b>
     * <br>When uploading an <u>image</u> you can reference said image using the specified filename as URI {@code attachment://filename.ext}.
     *
     * <p><u>Example</u>
     * <pre><code>
     * WebhookClient hook; // = reference of a WebhookClient such as interaction.getHook()
     * EmbedBuilder embed = new EmbedBuilder();
     * File file = new File("image.png"); // the name in your file system can be different from the name used in discord
     * embed.setImage("attachment://cat.png") // we specify this in sendFile as "cat.png"
     *      .setDescription("This is a cute cat :3");
     * hook.editMessageById(messageId, file, "cat.png").setEmbeds(embed.build()).queue();
     * </code></pre>
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  file
     *         The {@link File} data to upload to the webhook.
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws IllegalArgumentException
     *         If the provided file is {@code null}.
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageById(long messageId, @NotNull File file, @NotNull AttachmentOption... options)
    {
        return editMessageById(Long.toUnsignedString(messageId), file, options);
    }

    /**
     * Edit an existing message sent by this webhook.
     * <br>The provided file will be appended to the message. You cannot delete or edit existing files on a message.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p><b>Uploading images with Embeds</b>
     * <br>When uploading an <u>image</u> you can reference said image using the specified filename as URI {@code attachment://filename.ext}.
     *
     * <p><u>Example</u>
     * <pre><code>
     * WebhookClient hook; // = reference of a WebhookClient such as interaction.getHook()
     * EmbedBuilder embed = new EmbedBuilder();
     * File file = new File("image.png"); // the name in your file system can be different from the name used in discord
     * embed.setImage("attachment://cat.png") // we specify this in sendFile as "cat.png"
     *      .setDescription("This is a cute cat :3");
     * hook.editMessageById(messageId, file, "cat.png").setEmbeds(embed.build()).queue();
     * </code></pre>
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  file
     *         The {@link File} data to upload to the webhook.
     * @param  name
     *         The file name that should be sent to discord
     *         <br>Refer to the documentation for {@link #sendFile(File, String, AttachmentOption...)} for information about this parameter.
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws IllegalArgumentException
     *         If the provided file or filename is {@code null}.
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageById(long messageId, @NotNull File file, @NotNull String name, @NotNull AttachmentOption... options)
    {
        return editMessageById(Long.toUnsignedString(messageId), file, name, options);
    }

    /**
     * Edit an existing message sent by this webhook.
     * <br>The provided file will be appended to the message. You cannot delete or edit existing files on a message.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p><b>Uploading images with Embeds</b>
     * <br>When uploading an <u>image</u> you can reference said image using the specified filename as URI {@code attachment://filename.ext}.
     *
     * <p><u>Example</u>
     * <pre><code>
     * WebhookClient hook; // = reference of a WebhookClient such as interaction.getHook()
     * EmbedBuilder embed = new EmbedBuilder();
     * InputStream file = new FileInputStream("image.png"); // the name in your file system can be different from the name used in discord
     * embed.setImage("attachment://cat.png") // we specify this in sendFile as "cat.png"
     *      .setDescription("This is a cute cat :3");
     * hook.editMessageById(messageId, file, "cat.png").setEmbeds(embed.build()).queue();
     * </code></pre>
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The message id. For interactions this supports {@code "@original"} to edit the source message of the interaction.
     * @param  data
     *         The InputStream data to upload to the webhook.
     * @param  name
     *         The file name that should be sent to discord
     *         <br>Refer to the documentation for {@link #sendFile(File, String, AttachmentOption...)} for information about this parameter.
     * @param  options
     *         Possible options to apply to this attachment, such as marking it as spoiler image
     *
     * @throws IllegalArgumentException
     *         If the provided data or filename is {@code null}.
     *
     * @return {@link WebhookMessageUpdateAction}
     */
    @NotNull
    @CheckReturnValue
    default WebhookMessageUpdateAction<T> editMessageById(long messageId, @NotNull byte[] data, @NotNull String name, @NotNull AttachmentOption... options)
    {
        return editMessageById(Long.toUnsignedString(messageId), data, name, options);
    }


    /**
     * Delete a message from this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The id for the message to delete
     *
     * @throws IllegalArgumentException
     *         If the provided message id is null or not a valid snowflake
     *
     * @return {@link RestAction}
     */
    @NotNull
    @CheckReturnValue
    RestAction<Void> deleteMessageById(@NotNull String messageId);

    /**
     * Delete a message from this webhook.
     *
     * <p>If this is an {@link net.dv8tion.jda.api.interactions.InteractionHook InteractionHook} this method will be delayed until the interaction is acknowledged.
     *
     * <p>Possible {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The webhook is no longer available, either it was deleted or in case of interactions it expired.</li>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_MESSAGE UNKNOWN_MESSAGE}
     *     <br>The message for that id does not exist</li>
     * </ul>
     *
     * @param  messageId
     *         The id for the message to delete
     *
     * @return {@link RestAction}
     */
    @NotNull
    @CheckReturnValue
    default RestAction<Void> deleteMessageById(long messageId)
    {
        return deleteMessageById(Long.toUnsignedString(messageId));
    }

//    @Nonnull
//    static WebhookClient<WebhookMessageAction> createClient(@Nonnull JDA api, @Nonnull String url)
//    {
//        Checks.notNull(url, "URL");
//        Matcher matcher = Webhook.WEBHOOK_URL.matcher(url);
//        if (!matcher.matches())
//            throw new IllegalArgumentException("Provided invalid webhook URL");
//        String id = matcher.group(1);
//        String token = matcher.group(2);
//        return createClient(api, id, token);
//    }
//
//    @Nonnull
//    static WebhookClient<WebhookMessageAction> createClient(@Nonnull JDA api, @Nonnull String webhookId, @Nonnull String webhookToken)
//    {
//        Checks.notNull(api, "JDA");
//        Checks.notBlank(webhookToken, "Token");
//        return new AbstractWebhookClient<WebhookMessageAction>(MiscUtil.parseSnowflake(webhookId), webhookToken, api)
//        {
//            @Override
//            public WebhookMessageAction sendRequest()
//            {
//                Route.CompiledRoute route = Route.Webhooks.EXECUTE_WEBHOOK.compile(webhookId, webhookToken);
//                route = route.withQueryParams("wait", "true");
//                WebhookMessageActionImpl action = new WebhookMessageActionImpl(api, route);
//                action.run();
//                return action;
//            }
//
//            @Override
//            public WebhookMessageAction editRequest(String messageId)
//            {
//                Checks.isSnowflake(messageId);
//                Route.CompiledRoute route = Route.Webhooks.EXECUTE_WEBHOOK_EDIT.compile(webhookId, webhookToken, messageId);
//                route = route.withQueryParams("wait", "true");
//                WebhookMessageActionImpl action = new WebhookMessageActionImpl(api, route);
//                action.run();
//                return action;
//            }
//        };
//    }
}
