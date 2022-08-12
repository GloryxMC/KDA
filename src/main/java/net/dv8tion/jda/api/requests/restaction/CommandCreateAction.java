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

import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;

import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

/**
 * Specialized {@link RestAction} used to create or update commands.
 * <br>If a command with the specified name already exists, it will be replaced!
 *
 * <p>This operation is <b>not</b> idempotent!
 * Commands will persist between restarts of your bot, you only have to create a command once.
 */
public interface CommandCreateAction extends RestAction<Command>, SlashCommandData
{
    @NotNull
    @Override
    CommandCreateAction setCheck(@Nullable BooleanSupplier checks);

    @NotNull
    @Override
    CommandCreateAction addCheck(@NotNull BooleanSupplier checks);

    @NotNull
    @Override
    CommandCreateAction timeout(long timeout, @NotNull TimeUnit unit);

    @NotNull
    @Override
    @CheckReturnValue
    CommandCreateAction deadline(long timestamp);

    @NotNull
    @Override
    @CheckReturnValue
    CommandCreateAction setLocalizationFunction(@NotNull LocalizationFunction localizationFunction);

    @NotNull
    @Override
    @CheckReturnValue
    CommandCreateAction setName(@NotNull String name);

    @NotNull
    @Override
    @CheckReturnValue
    CommandCreateAction setNameLocalization(@NotNull DiscordLocale locale, @NotNull String name);

    @NotNull
    @Override
    @CheckReturnValue
    CommandCreateAction setNameLocalizations(@NotNull Map<DiscordLocale, String> map);

    @NotNull
    @Override
    @CheckReturnValue
    CommandCreateAction setDescription(@NotNull String description);

    @NotNull
    @Override
    @CheckReturnValue
    CommandCreateAction setDescriptionLocalization(@NotNull DiscordLocale locale, @NotNull String description);

    @NotNull
    @Override
    @CheckReturnValue
    CommandCreateAction setDescriptionLocalizations(@NotNull Map<DiscordLocale, String> map);

    @NotNull
    @Override
    @CheckReturnValue
    CommandCreateAction addOptions(@NotNull OptionData... options);

    @NotNull
    @Override
    @CheckReturnValue
    default CommandCreateAction addOptions(@NotNull Collection<? extends OptionData> options)
    {
        return (CommandCreateAction) SlashCommandData.super.addOptions(options);
    }

    @NotNull
    @Override
    @CheckReturnValue
    default CommandCreateAction addOption(@NotNull OptionType type, @NotNull String name, @NotNull String description, boolean required, boolean autoComplete)
    {
        return (CommandCreateAction) SlashCommandData.super.addOption(type, name, description, required, autoComplete);
    }

    @NotNull
    @Override
    @CheckReturnValue
    default CommandCreateAction addOption(@NotNull OptionType type, @NotNull String name, @NotNull String description, boolean required)
    {
        return (CommandCreateAction) SlashCommandData.super.addOption(type, name, description, required);
    }

    @NotNull
    @Override
    @CheckReturnValue
    default CommandCreateAction addOption(@NotNull OptionType type, @NotNull String name, @NotNull String description)
    {
        return (CommandCreateAction) SlashCommandData.super.addOption(type, name, description, false);
    }

    @NotNull
    @Override
    @CheckReturnValue
    CommandCreateAction addSubcommands(@NotNull SubcommandData... subcommands);

    @NotNull
    @Override
    @CheckReturnValue
    default CommandCreateAction addSubcommands(@NotNull Collection<? extends SubcommandData> subcommands)
    {
        return (CommandCreateAction) SlashCommandData.super.addSubcommands(subcommands);
    }

    @NotNull
    @Override
    @CheckReturnValue
    CommandCreateAction addSubcommandGroups(@NotNull SubcommandGroupData... groups);

    @NotNull
    @Override
    @CheckReturnValue
    default CommandCreateAction addSubcommandGroups(@NotNull Collection<? extends SubcommandGroupData> groups)
    {
        return (CommandCreateAction) SlashCommandData.super.addSubcommandGroups(groups);
    }

    @NotNull
    @Override
    @CheckReturnValue
    CommandCreateAction setDefaultPermissions(@NotNull DefaultMemberPermissions permission);

    @NotNull
    @Override
    @CheckReturnValue
    CommandCreateAction setGuildOnly(boolean guildOnly);
}
