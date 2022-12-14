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

import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

/**
 * Specialized {@link RestAction} used to replace existing commands of a guild or globally.
 * <br>Any commands that currently exist and are not listed through {@link #addCommands(CommandData...)} will be <b>DELETED</b>!
 *
 * <p>This operation is idempotent. Commands will persist between restarts of your bot, you only have to create a command once.
 */
public interface CommandListUpdateAction extends RestAction<List<Command>>
{
    @NotNull
    @Override
    CommandListUpdateAction timeout(long timeout, @NotNull TimeUnit unit);

    @NotNull
    @Override
    CommandListUpdateAction deadline(long timestamp);

    @NotNull
    @Override
    CommandListUpdateAction setCheck(@Nullable BooleanSupplier checks);

    @NotNull
    @Override
    CommandListUpdateAction addCheck(@NotNull BooleanSupplier checks);

    /**
     * Adds up to
     * {@value Commands#MAX_SLASH_COMMANDS} slash commands,
     * {@value Commands#MAX_USER_COMMANDS} user context commands, and
     * {@value Commands#MAX_MESSAGE_COMMANDS} message context commands.
     *
     * <p>When a command is not listed in this request, it will be deleted.
     *
     * @param  commands
     *         The {@link CommandData commands} to add
     *
     * @throws IllegalArgumentException
     *         If null or more than
     *         {@value Commands#MAX_SLASH_COMMANDS} slash commands,
     *         {@value Commands#MAX_USER_COMMANDS} user context commands, or
     *         {@value Commands#MAX_MESSAGE_COMMANDS} message context commands, are provided
     *
     * @return The CommandUpdateAction instance, for chaining
     *
     * @see    Commands#slash(String, String) Commands.slash(...)
     * @see    Commands#message(String) Commands.message(...)
     * @see    Commands#user(String) Commands.user(...)
     */
    @NotNull
    @CheckReturnValue
    CommandListUpdateAction addCommands(@NotNull Collection<? extends CommandData> commands);

    /**
     * Adds up to
     * {@value Commands#MAX_SLASH_COMMANDS} slash commands,
     * {@value Commands#MAX_USER_COMMANDS} user context commands, and
     * {@value Commands#MAX_MESSAGE_COMMANDS} message context commands.
     *
     * <p>When a command is not listed in this request, it will be deleted.
     *
     * @param  commands
     *         The {@link CommandData commands} to add
     *
     * @throws IllegalArgumentException
     *         If null or more than
     *         {@value Commands#MAX_SLASH_COMMANDS} slash commands,
     *         {@value Commands#MAX_USER_COMMANDS} user context commands, or
     *         {@value Commands#MAX_MESSAGE_COMMANDS} message context commands, are provided
     *
     * @return The CommandUpdateAction instance, for chaining
     *
     * @see    Commands#slash(String, String) Commands.slash(...)
     * @see    Commands#message(String) Commands.message(...)
     * @see    Commands#user(String) Commands.user(...)
     */
    @NotNull
    @CheckReturnValue
    default CommandListUpdateAction addCommands(@NotNull CommandData... commands)
    {
        Checks.noneNull(commands, "Command");
        return addCommands(Arrays.asList(commands));
    }
}
