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

package net.dv8tion.jda.api.events.thread;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ThreadChannel;
import net.dv8tion.jda.api.events.Event;
import org.jetbrains.annotations.NotNull;

//TODO-v5: Docs
public class GenericThreadEvent extends Event
{
    protected final ThreadChannel thread;

    public GenericThreadEvent(@NotNull JDA api, long responseNumber, ThreadChannel thread)
    {
        super(api, responseNumber);

        this.thread = thread;
    }

    @NotNull
    public ThreadChannel getThread()
    {
        return thread;
    }

    @NotNull
    public Guild getGuild()
    {
        return thread.getGuild();
    }
}
