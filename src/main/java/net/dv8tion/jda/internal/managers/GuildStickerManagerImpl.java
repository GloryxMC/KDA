package net.dv8tion.jda.internal.managers;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.sticker.StickerSnowflake;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.managers.GuildStickerManager;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.requests.Route;
import net.dv8tion.jda.internal.utils.Checks;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class GuildStickerManagerImpl extends ManagerBase<GuildStickerManager> implements GuildStickerManager
{
    private final Guild guild;
    private final long guildId;
    private String name;
    private String description;
    private String tags;

    public GuildStickerManagerImpl(Guild guild, long guildId, StickerSnowflake sticker)
    {
        super(guild.getJDA(), Route.Stickers.MODIFY_GUILD_STICKER.compile(Long.toUnsignedString(guildId), sticker.getId()));
        this.guild = guild;
        this.guildId = guildId;
        if (isPermissionChecksEnabled())
            checkPermissions();
    }

    @Nullable
    @Override
    public Guild getGuild()
    {
        return guild;
    }

    @Override
    public long getGuildIdLong()
    {
        return guildId;
    }

    @NotNull
    @Override
    public GuildStickerManagerImpl reset(long fields)
    {
        super.reset(fields);
        if ((fields & NAME) == NAME)
            this.name = null;
        if ((fields & DESCRIPTION) == DESCRIPTION)
            this.description = null;
        if ((fields & TAGS) == TAGS)
            this.tags = null;
        return this;
    }

    @NotNull
    @Override
    public GuildStickerManagerImpl reset(long... fields)
    {
        super.reset(fields);
        return this;
    }

    @NotNull
    @Override
    public GuildStickerManagerImpl reset()
    {
        super.reset();
        this.name = null;
        this.description = null;
        this.tags = null;
        return this;
    }

    @NotNull
    @Override
    public GuildStickerManager setName(@NotNull String name)
    {
        Checks.inRange(name, 2, 30, "Name");
        this.name = name;
        set |= NAME;
        return this;
    }

    @NotNull
    @Override
    public GuildStickerManager setDescription(@NotNull String description)
    {
        Checks.inRange(description, 2, 100, "Description");
        this.description = description;
        set |= DESCRIPTION;
        return this;
    }

    @NotNull
    @Override
    public GuildStickerManager setTags(@NotNull Collection<String> tags)
    {
        Checks.notEmpty(tags, "Tags");
        for (String tag : tags)
            Checks.notEmpty(tag, "Tags"); // checks for empty and null
        String csv = String.join(",", tags);
        Checks.notLonger(csv, 200, "List of tags");
        this.tags = csv;
        set |= TAGS;
        return this;
    }

    @Override
    protected RequestBody finalizeData()
    {
        DataObject object = DataObject.empty();
        if (shouldUpdate(NAME))
            object.put("name", name);
        if (shouldUpdate(DESCRIPTION))
            object.put("description", description);
        if (shouldUpdate(TAGS))
            object.put("tags", tags);
        reset();
        return getRequestBody(object);
    }

    @Override
    protected boolean checkPermissions()
    {
        if (guild != null && !guild.getSelfMember().hasPermission(Permission.MANAGE_EMOTES_AND_STICKERS))
            throw new InsufficientPermissionException(guild, Permission.MANAGE_EMOTES_AND_STICKERS);
        return super.checkPermissions();
    }
}
