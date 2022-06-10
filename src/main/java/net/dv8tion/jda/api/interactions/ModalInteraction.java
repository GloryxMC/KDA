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

package net.dv8tion.jda.api.interactions;

import net.dv8tion.jda.api.interactions.callbacks.IMessageEditCallback;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Interaction on a {@link Modal}
 *
 * <p>If the modal of this interaction was a reply to a {@link net.dv8tion.jda.api.interactions.components.ComponentInteraction ComponentInteraction},
 * you can also use {@link #deferEdit()} to edit the original message that contained the component instead of replying.
 *
 * @see    net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
 */
public interface ModalInteraction extends IReplyCallback, IMessageEditCallback
{
    /**
     * Returns the custom id of the Modal in question
     *
     * @return Custom id
     * 
     * @see    Modal.Builder#setId(String)
     */
    @NotNull
    String getModalId();

    /**
     * Returns a List of {@link net.dv8tion.jda.api.interactions.modals.ModalMapping ModalMappings} representing the values input by the user for each field when the modal was submitted.
     *
     * @return Immutable List of {@link net.dv8tion.jda.api.interactions.modals.ModalMapping ModalMappings}
     *
     * @see    #getValue(String)
     */
    @NotNull
    List<ModalMapping> getValues();

    /**
     * Convenience method to get a {@link net.dv8tion.jda.api.interactions.modals.ModalMapping ModalMapping} by its id from the List of {@link net.dv8tion.jda.api.interactions.modals.ModalMapping ModalMappings}
     *
     * <p>Returns null if no component with that id has been found
     *
     * @param  id
     *         The custom id
     *
     * @throws IllegalArgumentException
     *         If the provided id is null
     *
     * @return ModalMapping with this id, or null if not found
     *
     * @see    #getValues()
     */
    @Nullable
    default ModalMapping getValue(@NotNull String id)
    {
        Checks.notNull(id, "ID");
        return getValues().stream()
                .filter(mapping -> mapping.getId().equals(id))
                .findFirst().orElse(null);
    }
}
