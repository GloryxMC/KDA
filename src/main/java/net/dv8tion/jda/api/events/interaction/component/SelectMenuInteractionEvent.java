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

package net.dv8tion.jda.api.events.interaction.component;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenuInteraction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Indicates that a custom {@link SelectMenu} on one of the bots messages was used by a user.
 *
 * <p>This fires when a user selects the options on one of the custom select menus attached to a bot or webhook message.
 *
 * <p><b>Requirements</b><br>
 * To receive these events, you must unset the <b>Interactions Endpoint URL</b> in your application dashboard.
 * You can simply remove the URL for this endpoint in your settings at the <a href="https://discord.com/developers/applications" target="_blank">Discord Developers Portal</a>.
 */
public class SelectMenuInteractionEvent extends GenericComponentInteractionCreateEvent implements SelectMenuInteraction
{
    private final SelectMenuInteraction menuInteraction;

    public SelectMenuInteractionEvent(@NotNull JDA api, long responseNumber, @NotNull SelectMenuInteraction interaction)
    {
        super(api, responseNumber, interaction);
        this.menuInteraction = interaction;
    }

    @NotNull
    @Override
    public SelectMenuInteraction getInteraction()
    {
        return menuInteraction;
    }

    @NotNull
    @Override
    public SelectMenu getComponent()
    {
        return menuInteraction.getComponent();
    }

    @NotNull
    @Override
    public List<String> getValues()
    {
        return menuInteraction.getValues();
    }
}
