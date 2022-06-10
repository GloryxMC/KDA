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
import org.apache.commons.collections4.Bag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface to access the mentions of various entities.
 */
public interface Mentions
{
    /**
     * The corresponding JDA instance
     *
     * @return The jda instance
     */
    @NotNull
    JDA getJDA();

    /**
     * Indicates if everyone is mentioned, by either using {@code @everyone} or {@code @here}.
     *
     * <p>This is different from checking if {@code @everyone} is in the string, since mentions require additional flags to trigger.
     *
     * @return True, if everyone is mentioned
     */
    boolean mentionsEveryone();

    /**
     * An immutable list of all mentioned {@link User Users}.
     * <br>If no user was mentioned, this list is empty. Elements are sorted in order of appearance. This only
     * counts direct mentions of the user and not mentions through roles or everyone mentions.
     *
     * <p>This might also contain users which are not present in {@link #getMembers()}.
     *
     * @return Immutable list of mentioned users
     */
    @NotNull
    List<User> getUsers();

    /**
     * A {@link Bag Bag} of mentioned {@link User Users}.
     * <br>This can be used to retrieve the amount of times a user was mentioned. This only
     * counts direct mentions of the user and not mentions through roles or everyone mentions.
     *
     * <p>This might also contain users which are not present in {@link #getMembers()}.
     *
     * <p><b>Example</b><br>
     * <pre>{@code
     * void sendCount(Message msg)
     * {
     *     List<User> mentions = msg.getMentions().getUsers(); // distinct list, in order of appearance
     *     Bag<User> count = msg.getMentions().getUsersBag();
     *     StringBuilder content = new StringBuilder();
     *     for (User user : mentions)
     *     {
     *         content.append(user.getAsTag())
     *                .append(": ")
     *                .append(count.getCount(user))
     *                .append("\n");
     *     }
     *     msg.getChannel().sendMessage(content.toString()).queue();
     * }
     * }</pre>
     *
     * @return {@link Bag Bag} of mentioned users
     *
     * @see    #getUsers()
     */
    @NotNull
    Bag<User> getUsersBag();

    /**
     * An immutable list of all mentioned {@link GuildChannel GuildChannels}.
     * <br>If none were mentioned, this list is empty. Elements are sorted in order of appearance.
     *
     * <p><b>This may include GuildChannels from other {@link Guild Guilds}</b>
     *
     * @return Immutable list of mentioned GuildChannels
     */
    @NotNull
    List<GuildChannel> getChannels();

    /**
     * A {@link Bag Bag} of mentioned channels.
     * <br>This can be used to retrieve the amount of times a channel was mentioned.
     *
     * <p><b>This may include GuildChannels from other {@link Guild Guilds}</b>
     *
     * <p><b>Example</b><br>
     * <pre>{@code
     * void sendCount(Message msg)
     * {
     *     Bag<GuildChannel> mentions = msg.getMentions().getChannelsBag();
     *     StringBuilder content = new StringBuilder();
     *     for (GuildChannel channel : mentions.uniqueSet())
     *     {
     *         content.append("#")
     *                .append(channel.getName())
     *                .append(": ")
     *                .append(mentions.getCount(channel))
     *                .append("\n");
     *     }
     *     msg.getChannel().sendMessage(content.toString()).queue();
     * }
     * }</pre>
     *
     * @return {@link Bag Bag} of mentioned channels
     *
     * @see    #getChannels()
     */
    @NotNull
    Bag<GuildChannel> getChannelsBag();

    /**
     * An immutable list of all mentioned {@link GuildChannel GuildChannels} of type {@code clazz}.
     * <br>If none were mentioned, this list is empty. Elements are sorted in order of appearance.
     *
     * <p><b>This may include GuildChannels from other {@link Guild Guilds}</b>
     *
     * <p><b>Example</b><br>
     * <pre>{@code
     * List<GuildMessageChannel> getCoolMessageChannels(Message msg)
     * {
     *     List<GuildMessageChannel> channels = msg.getMentions().getChannels(GuildMessageChannel.class);
     *     return channels.stream()
     *         .filter(channel -> channel.getName().contains("cool"))
     *         .collect(Collectors.toList());
     * }
     * }</pre>
     *
     * @param  clazz
     *         The {@link GuildChannel} sub-class {@link Class class object} of the type of channel desired
     *
     * @throws IllegalArgumentException
     *         If {@code clazz} is {@code null}
     *
     * @return Immutable list of mentioned GuildChannels that are of type {@code clazz}.
     */
    @NotNull
    <T extends GuildChannel> List<T> getChannels(@NotNull Class<T> clazz);

    /**
     * A {@link Bag Bag} of mentioned channels of type {@code clazz}.
     * <br>This can be used to retrieve the amount of times a channel was mentioned.
     *
     * <p><b>This may include GuildChannels from other {@link Guild Guilds}</b>
     *
     * <p><b>Example</b><br>
     * <pre>{@code
     * void sendCount(Message msg)
     * {
     *     Bag<GuildMessageChannel> mentions = msg.getMentions().getChannelsBag(GuildMessageChannel.class);
     *     StringBuilder content = new StringBuilder();
     *     for (GuildMessageChannel channel : mentions.uniqueSet())
     *     {
     *         content.append("#")
     *                .append(channel.getName())
     *                .append(": ")
     *                .append(mentions.getCount(channel))
     *                .append("\n");
     *     }
     *     msg.getChannel().sendMessage(content.toString()).queue();
     * }
     * }</pre>
     *
     * @param  clazz
     *         The {@link GuildChannel} sub-class {@link Class class object} of the type of channel desired
     *
     * @throws IllegalArgumentException
     *         If {@code clazz} is {@code null}
     *
     * @return {@link Bag Bag} of mentioned channels of type {@code clazz}
     *
     * @see    #getChannels(Class)
     */
    @NotNull
    <T extends GuildChannel> Bag<T> getChannelsBag(@NotNull Class<T> clazz);

    /**
     * An immutable list of all mentioned {@link Role Roles}.
     * <br>If none were mentioned, this list is empty. Elements are sorted in order of appearance. This only
     * counts direct mentions of the role and not mentions through everyone mentions.
     *
     * <p><b>This may include Roles from other {@link Guild Guilds}</b>
     *
     * @return immutable list of mentioned Roles
     */
    @NotNull
    List<Role> getRoles();

    /**
     * A {@link Bag Bag} of mentioned roles.
     * <br>This can be used to retrieve the amount of times a role was mentioned. This only
     * counts direct mentions of the role and not mentions through everyone mentions.
     *
     * <p><b>This may include Roles from other {@link Guild Guilds}</b>
     *
     * <p><b>Example</b><br>
     * <pre>{@code
     * void sendCount(Message msg)
     * {
     *     List<Role> mentions = msg.getMentions().getRoles(); // distinct list, in order of appearance
     *     Bag<Role> count = msg.getMentions().getRolesBag();
     *     StringBuilder content = new StringBuilder();
     *     for (Role role : mentions)
     *     {
     *         content.append(role.getName())
     *                .append(": ")
     *                .append(count.getCount(role))
     *                .append("\n");
     *     }
     *     msg.getChannel().sendMessage(content.toString()).queue();
     * }
     * }</pre>
     *
     * @return {@link Bag Bag} of mentioned roles
     *
     * @see    #getRoles()
     */
    @NotNull
    Bag<Role> getRolesBag();

    /**
     * All {@link Emote Emotes} used.
     * <br><b>This only includes Custom Emotes, not unicode Emojis.</b> JDA classifies Emotes as the Custom Emojis uploaded
     * to a Guild and retrievable with {@link Guild#getEmotes()}. These are not the same
     * as the unicode emojis that Discord also supports. Elements are sorted in order of appearance.
     *
     * <p><b><u>Unicode emojis are not included as {@link Emote Emote}!</u></b>
     *
     * @return An immutable list of the Emotes used (example match {@literal <:jda:230988580904763393>})
     */
    @NotNull
    List<Emote> getEmotes();

    /**
     * A {@link Bag Bag} of emotes used.
     * <br>This can be used to retrieve the amount of times an emote was used.
     *
     * <p><b>Example</b><br>
     * <pre>{@code
     * void sendCount(Message msg)
     * {
     *     List<Emote> emotes = msg.getMentions().getEmotes(); // distinct list, in order of appearance
     *     Bag<Emote> count = msg.getMentions().getEmotesBag();
     *     StringBuilder content = new StringBuilder();
     *     for (Emote emote : emotes)
     *     {
     *         content.append(emote.getName())
     *                .append(": ")
     *                .append(count.getCount(role))
     *                .append("\n");
     *     }
     *     msg.getChannel().sendMessage(content.toString()).queue();
     * }
     * }</pre>
     *
     * @return {@link Bag Bag} of used emotes
     *
     * @see    #getEmotes()
     */
    @NotNull
    Bag<Emote> getEmotesBag();

    /**
     * An immutable list of all mentioned {@link Member Members}.
     * <br>If none were mentioned, this list is empty. Elements are sorted in order of appearance. This only
     * counts direct mentions of the role and not mentions through everyone mentions.
     *
     * <p>This is always empty in {@link PrivateChannel PrivateChannels}.
     *
     * @return Immutable list of mentioned Members, or an empty list
     */
    @NotNull
    List<Member> getMembers();

    /**
     * A {@link Bag Bag} of mentioned {@link Member Members}.
     * <br>This can be used to retrieve the amount of times a user was mentioned. This only
     * counts direct mentions of the member and not mentions through roles or everyone mentions.
     *
     * <p><b>Example</b><br>
     * <pre>{@code
     * void sendCount(Message msg)
     * {
     *     List<Member> mentions = msg.getMentions().getMembers(); // distinct list, in order of appearance
     *     Bag<Member> count = msg.getMentions().getMembersBag();
     *     StringBuilder content = new StringBuilder();
     *     for (Member user : mentions)
     *     {
     *         content.append(member.getUser().getAsTag())
     *                .append(": ")
     *                .append(count.getCount(member))
     *                .append("\n");
     *     }
     *     msg.getChannel().sendMessage(content.toString()).queue();
     * }
     * }</pre>
     *
     * @return {@link Bag Bag} of mentioned members
     *
     * @see    #getMembers()
     */
    @NotNull
    Bag<Member> getMembersBag();

    /**
     * Combines all instances of {@link IMentionable IMentionable}
     * filtered by the specified {@link Message.MentionType MentionType} values.
     * <br>If a {@link Member} is available, it will be taken in favor of a {@link User}.
     * This only provides either the Member or the User instance, rather than both.
     *
     * <p>If no MentionType values are given, all types are used.
     *
     * @param  types
     *         {@link Message.MentionType MentionTypes} to include
     *
     * @throws IllegalArgumentException
     *         If provided with {@code null}
     *
     * @return Immutable list of filtered {@link IMentionable IMentionable} instances
     */
    @NotNull
    List<IMentionable> getMentions(@NotNull Message.MentionType... types);

    /**
     * Checks if given {@link IMentionable IMentionable}
     * was mentioned in any way (@User, @everyone, @here, @Role).
     * <br>If no filtering {@link Message.MentionType MentionTypes} are
     * specified, all types are used.
     *
     * <p>{@link Message.MentionType#HERE MentionType.HERE} and {@link Message.MentionType#EVERYONE MentionType.EVERYONE}
     * will only be checked, if the given {@link IMentionable IMentionable} is of type
     * {@link User User} or {@link Member Member}.
     * <br>Online status of Users/Members is <b>NOT</b> considered when checking {@link Message.MentionType#HERE MentionType.HERE}.
     *
     * @param  mentionable
     *         The mentionable entity to check on.
     * @param  types
     *         The types to include when checking whether this type was mentioned.
     *         This will be used with {@link #getMentions(Message.MentionType...) getMentions(MentionType...)}
     *
     * @return True, if the given mentionable was mentioned in this message
     */
    boolean isMentioned(@NotNull IMentionable mentionable, @NotNull Message.MentionType... types);
}
