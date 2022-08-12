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

package net.dv8tion.jda.api.events.interaction.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.privileges.IntegrationPrivilege;
import net.dv8tion.jda.api.interactions.commands.privileges.PrivilegeTargetType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Indicates that the {@link IntegrationPrivilege Privileges} of an application-command on a guild changed.
 * <br>If the moderator updates application-wide privileges instead of command, a {@link ApplicationUpdatePrivilegesEvent} will be fired instead.
 *
 * <p>Can be used to get affected Guild and {@link List} of new {@link IntegrationPrivilege Privileges}
 */
public class ApplicationCommandUpdatePrivilegesEvent extends GenericPrivilegeUpdateEvent
{
    public ApplicationCommandUpdatePrivilegesEvent(@NotNull JDA api, long responseNumber, @NotNull Guild guild,
                                                   long targetId, long applicationId, @NotNull List<IntegrationPrivilege> privileges)
    {
        super(api, responseNumber, guild, targetId, applicationId, privileges);
    }

    @NotNull
    @Override
    public PrivilegeTargetType getTargetType()
    {
        return PrivilegeTargetType.COMMAND;
    }

    /**
     * The id of the command whose privileges have been changed.
     *
     * @return id of the command whose privileges have been changed.
     */
    public long getCommandIdLong()
    {
        return getTargetIdLong();
    }

    /**
     * The id of the command whose privileges have been changed.
     *
     * @return id of the command whose privileges have been changed.
     */
    @NotNull
    public String getCommandId()
    {
        return getTargetId();
    }
}
