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
package net.dv8tion.jda.api.entities

import kotlinx.serialization.*
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.utils.AttachmentProxy
import net.dv8tion.jda.api.utils.ImageProxy
import net.dv8tion.jda.internal.utils.Helpers
import net.dv8tion.jda.api.utils.data.DataArray
import net.dv8tion.jda.api.utils.data.DataObject
import net.dv8tion.jda.api.utils.data.SerializableData
import net.gloryx.kda.HexSerializer
import java.awt.Color
import net.gloryx.kda.ODTSer
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * Represents an embed displayed by Discord.
 * <br></br>A visual representation of an Embed can be found at:
 * [Embed Overview](https://raw.githubusercontent.com/DV8FromTheWorld/JDA/assets/assets/docs/embeds/01-Overview.png)
 * <br></br>This class has many possibilities for null values, so be careful!
 *
 * @see EmbedBuilder
 *
 * @see Message.getEmbeds
 */
@Serializable
data class MessageEmbed(
        /**
         * The url that was originally placed into chat that spawned this embed.
         * <br></br>**This will return the [title url][.getTitle] if the [type][.getType] of this embed is [RICH][EmbedType.RICH].**
         *
         * @return Possibly-null String containing the link that spawned this embed or the title url
         */
        @Transient
        val url: String? = null,
        /**
         * The title of the embed. Typically, this will be the html title of the webpage that is being embedded.<br></br>
         * If no title could be found, like the case of [EmbedType] = [IMAGE][EmbedType.IMAGE],
         * this method will return null.
         *
         * @return Possibly-null String containing the title of the embedded resource.
         */
        val title: String? = null,
        /**
         * The description of the embedded resource.
         * <br></br>This is provided only if Discord could find a description for the embedded resource using the provided url.
         * <br></br>Commonly, this is null. Be careful when using it.
         *
         * @return Possibly-null String containing a description of the embedded resource.
         */
        val description: String? = null,
        /**
         * The [EmbedType] of this embed.
         *
         * @return The [EmbedType] of this embed.
         */
        val type: EmbedType = EmbedType.RICH,
        /**
         * The timestamp of the embed.
         *
         * @return Possibly-null OffsetDateTime object representing the timestamp.
         */
        @Serializable(ODTSer::class)
        val timestamp: OffsetDateTime? = null,
        /**
         * The raw RGB color value for this embed
         * <br></br>Defaults to [Role.DEFAULT_COLOR_RAW] if no color is set
         *
         * @return The raw RGB color value or default
         */
        @Transient
        val colorRaw: Int = Role.DEFAULT_COLOR_RAW,
        /**
         * The information about the [Thumbnail][net.dv8tion.jda.api.entities.MessageEmbed.Thumbnail] image to be displayed with the embed.
         * <br></br>If a [Thumbnail][net.dv8tion.jda.api.entities.MessageEmbed.Thumbnail] was not part of this embed, this returns null.
         *
         * @return Possibly-null [Thumbnail][net.dv8tion.jda.api.entities.MessageEmbed.Thumbnail] instance
         * containing general information on the displayable thumbnail.
         */
        val thumbnail: Thumbnail? = null,
        /**
         * The information on site from which the embed was generated from.
         * <br></br>If Discord did not generate any deliverable information about the site, this returns null.
         *
         * @return Possibly-null [Provider][net.dv8tion.jda.api.entities.MessageEmbed.Provider]
         * containing site information.
         */
        @Transient
        val siteProvider: Provider? = null,
        /**
         * The information on the creator of the embedded content.
         * <br></br>This is typically used to represent the account on the providing site.
         *
         * @return Possibly-null [AuthorInfo][net.dv8tion.jda.api.entities.MessageEmbed.AuthorInfo]
         * containing author information.
         */
        val author: AuthorInfo? = null,
        /**
         * The information about the video which should be displayed as an embed.
         * <br></br>This is used when sites with HTML5 players are linked and embedded. Most commonly Youtube.
         * <br></br>If this [EmbedType] != [VIDEO][EmbedType.VIDEO]
         * this will always return null.
         *
         * @return Possibly-null [VideoInfo][net.dv8tion.jda.api.entities.MessageEmbed.VideoInfo]
         * containing the information about the video which should be embedded.
         */
        @SerialName("video")
        val videoInfo: VideoInfo? = null,
        /**
         * The footer (bottom) of the embedded content.
         * <br></br>This is typically used for timestamps or site icons.
         *
         * @return Possibly-null [Footer][net.dv8tion.jda.api.entities.MessageEmbed.Footer]
         * containing the embed footer content.
         */
        val footer: Footer? = null,
        /**
         * The information about the image in the message embed
         *
         * @return Possibly-null [ImageInfo][net.dv8tion.jda.api.entities.MessageEmbed.ImageInfo]
         * containing image information.
         */
        val image: ImageInfo? = null,
        /**
         * The fields in a message embed.
         * <br></br>Message embeds can contain multiple fields, each with a name, value, and a boolean
         * to determine if it will fall in-line with other fields. If the embed contains no
         * fields, an empty list will be returned.
         *
         * @return Never-null (but possibly empty) immutable [List] of [Field][net.dv8tion.jda.api.entities.MessageEmbed.Field] objects
         * containing field information.
         */
        val fields: List<Field> = listOf()
) : SerializableData {
    @Serializable(with = HexSerializer::class)
    val color = Color(colorRaw)

    @Transient
    private val mutex = Any()

    @Volatile
    @Transient
    private var length: Int = -1
    fun getLength(): Int {
        if (length > -1) return length
        synchronized(mutex) {
            if (length > -1) return length
            length = 0
            if (title != null) length += Helpers.codePointLength(title)
            if (description != null) length += Helpers.codePointLength(description.trim { it <= ' ' })
            if (author != null) length += Helpers.codePointLength(author.name)
            if (footer != null) length += Helpers.codePointLength(footer.text)
            for (f in fields) length += Helpers.codePointLength(
                    f.name
            ) + Helpers.codePointLength(f.value)
            return length
        }
    }

    @Volatile
    @Transient
    private var json: DataObject? = null

    /**
     * Whether this embed is empty.
     *
     * @return True, if this embed has no content
     */
    val isEmpty: Boolean
        get() = colorRaw == Role.DEFAULT_COLOR_RAW && timestamp == null && image == null && thumbnail == null && length == 0

    /**
     * The total amount of characters that is displayed when this embed is displayed by the Discord client.
     *
     *
     * The total character limit is defined by [.EMBED_MAX_LENGTH_BOT] as {@value #EMBED_MAX_LENGTH_BOT}.
     *
     * @return A never-negative sum of all displayed text characters.
     */

    /**
     * Whether this MessageEmbed can be used in a message.
     *
     *
     * The total character limit is defined by [.EMBED_MAX_LENGTH_BOT] as {@value #EMBED_MAX_LENGTH_BOT}.
     *
     * @return True, if this MessageEmbed can be used to send messages
     *
     * @see getLength
     */
    val isSendable: Boolean
        get() {
            if (isEmpty) return false
            return length <= EMBED_MAX_LENGTH_BOT
        }

    override fun equals(other: Any?): Boolean {
        if (other !is MessageEmbed) return false
        if (other === this) return true
        return ((url == other.url && title == other.title && description == other.description && type == other.type && thumbnail == other.thumbnail && siteProvider == other.siteProvider && author == other.author && videoInfo == other.videoInfo && footer == other.footer && image == other.image && colorRaw and 0xFFFFFF == other.colorRaw and 0xFFFFFF && timestamp == other.timestamp
                && Helpers.deepEquals(fields, other.fields)))
    }

    /**
     * Creates a new [net.dv8tion.jda.utils.data.DataObject]
     * used for sending.
     *
     * @return JSONObject for this embed
     */
    override fun toData(): DataObject {
        if (json != null) return json as DataObject
        synchronized(mutex) {
            if (json != null) return json as DataObject
            val obj = DataObject.empty()
            if (url != null) obj.put("url", url)
            if (title != null) obj.put("title", title)
            if (description != null) obj.put("description", description)
            if (timestamp != null) obj.put("timestamp", timestamp.format(DateTimeFormatter.ISO_INSTANT))
            if (colorRaw != Role.DEFAULT_COLOR_RAW) obj.put("color", colorRaw and 0xFFFFFF)
            if (thumbnail != null) obj.put("thumbnail", DataObject.empty().put("url", thumbnail.url))
            if (siteProvider != null) {
                val siteProviderObj = DataObject.empty()
                if (siteProvider.name != null) siteProviderObj.put("name", siteProvider.name)
                if (siteProvider.url != null) siteProviderObj.put("url", siteProvider.url)
                obj.put("provider", siteProviderObj)
            }
            if (author != null) {
                val authorObj = DataObject.empty()
                if (author.name != null) authorObj.put("name", author.name)
                if (author.url != null) authorObj.put("url", author.url)
                if (author.iconUrl != null) authorObj.put("icon_url", author.iconUrl)
                obj.put("author", authorObj)
            }
            if (videoInfo != null) obj.put("video", DataObject.empty().put("url", videoInfo.url))
            if (footer != null) {
                val footerObj = DataObject.empty()
                if (footer.text != null) footerObj.put("text", footer.text)
                if (footer.iconUrl != null) footerObj.put("icon_url", footer.iconUrl)
                obj.put("footer", footerObj)
            }
            if (image != null) obj.put("image", DataObject.empty().put("url", image.url))
            if (fields.isNotEmpty()) {
                val fieldsArray = DataArray.empty()
                for (field in fields) {
                    fieldsArray
                            .add(
                                    DataObject.empty()
                                            .put("name", field.name)
                                            .put("value", field.value)
                                            .put("inline", field.isInline)
                            )
                }
                obj.put("fields", fieldsArray)
            }
            return obj.also { json = it }
        }
    }

    override fun hashCode(): Int {
        var result = url?.hashCode() ?: 0
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        result = 31 * result + (timestamp?.hashCode() ?: 0)
        result = 31 * result + colorRaw
        result = 31 * result + (thumbnail?.hashCode() ?: 0)
        result = 31 * result + (siteProvider?.hashCode() ?: 0)
        result = 31 * result + (author?.hashCode() ?: 0)
        result = 31 * result + (videoInfo?.hashCode() ?: 0)
        result = 31 * result + (footer?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + mutex.hashCode()
        result = 31 * result + length
        result = 31 * result + (json?.hashCode() ?: 0)
        return result
    }

    fun head(title: String? = this.title, description: String? = this.description) = copy(title = title, description = description)
    fun field(name: String = "", value: String = "", inline: Boolean = false) = copy(fields = fields + Field(name, value, inline))

    /**
     * Represents the information Discord provided about a thumbnail image that should be
     * displayed with an embed message.
     */
    @Serializable
    data class Thumbnail(
            /**
             * The web url of this thumbnail image.
             *
             * @return Possibly-null String containing the url of the displayed image.
             */
            val url: String? = null,
            /**
             * The Discord proxied url of the thumbnail image.
             * <br></br>This url is used to access the image through Discord instead of directly to prevent ip scraping.
             *
             * @return Possibly-null String containing the proxied url of this image.
             */
            @Transient
            val proxyUrl: String? = null,
            /**
             * The width of the thumbnail image.
             *
             * @return Never-negative, Never-zero int containing the width of the image.
             */
            val width: Int = 1920,
            /**
             * The height of the thumbnail image.
             *
             * @return Never-negative, Never-zero int containing the height of the image.
             */
            val height: Int = 1080
    ) {
        /**
         * Returns an [AttachmentProxy] for this embed thumbnail.
         *
         * @return Possibly-null [AttachmentProxy] of this embed thumbnail
         *
         * @see .getProxyUrl
         */
        val proxy: AttachmentProxy?
            get() {
                return proxyUrl?.let { AttachmentProxy(it) }
            }
    }

    /**
     * Multipurpose class that represents a provider of content,
     * whether directly through creation or indirectly through hosting.
     */
    @Serializable
    data class Provider(
            /**
             * The name of the provider.
             * <br></br>If this is an author, most likely the author's username.
             * <br></br>If this is a website, most likely the site's name.
             *
             * @return Possibly-null String containing the name of the provider.
             */
            val name: String? = null,
            /**
             * The url of the provider.
             *
             * @return Possibly-null String containing the url of the provider.
             */
            val url: String? = null
    )

    /**
     * Represents the information provided to embed a video.
     * <br></br>The videos represented are expected to be played using an HTML5 player from the
     * site which the url belongs to.
     */
    @Serializable
    data class VideoInfo(
            /**
             * The url of the video.
             *
             * @return Possibly-null String containing the video url.
             */
            val url: String? = null,
            /**
             * The width of the video.
             * <br></br>This usually isn't the actual video width, but instead the starting embed window size.
             *
             *
             * Basically: Don't rely on this to represent the actual video's quality or size.
             *
             * @return Non-negative, Non-zero int containing the width of the embedded video.
             */
            val width: Int,
            /**
             * The height of the video.
             * <br></br>This usually isn't the actual video height, but instead the starting embed window size.
             *
             *
             * Basically: Don't rely on this to represent the actual video's quality or size.
             *
             * @return
             * Non-negative, Non-zero int containing the height of the embedded video.
             */
            val height: Int
    ) {

        override fun equals(other: Any?): Boolean {
            if (other !is VideoInfo) return false
            return other === this || other.url == url && other.width == width && other.height == height
        }

        override fun hashCode(): Int {
            var result = url?.hashCode() ?: 0
            result = 31 * result + width.hashCode()
            result = 31 * result + height.hashCode()
            return result
        }
    }

    /**
     * Represents the information provided to embed an image.
     */
    @Serializable
    data class ImageInfo(
            /**
             * The url of the image.
             *
             * @return Possibly-null String containing the image url.
             */
            val url: String? = null,
            /**
             * The url of the image, proxied by Discord
             * <br></br>This url is used to access the image through Discord instead of directly to prevent ip scraping.
             *
             * @return Possibly-null String containing the proxied image url.
             */
            @Transient
            val proxyUrl: String? = null,
            /**
             * The width of the image.
             *
             * @return Non-negative, Non-zero int containing the width of the embedded image.
             */
            val width: Int,
            /**
             * The height of the image.
             *
             * @return Non-negative, Non-zero int containing the height of the embedded image.
             */
            val height: Int
    ) {
        /**
         * Returns an [AttachmentProxy] for this embed image.
         *
         * @return Possibly-null [AttachmentProxy] of this embed image
         *
         * @see .getProxyUrl
         */
        val proxy: AttachmentProxy?
            get() {
                return proxyUrl?.let { AttachmentProxy(it) }
            }

        override fun equals(other: Any?): Boolean {
            if (other !is ImageInfo) return false
            return (other === this || other.url == url && other.proxyUrl == proxyUrl && other.width == width && other.height == height)
        }

        override fun hashCode(): Int {
            var result = url?.hashCode() ?: 0
            result = 31 * result + width.hashCode()
            result = 31 * result + height.hashCode()
            return result
        }
    }

    /**
     * Class that represents the author of content, possibly including an icon
     * that Discord proxies.
     */
    @Serializable
    data class AuthorInfo(
            /**
             * The name of the Author.
             * <br></br>This is most likely the name of the account associated with the embed
             *
             * @return Possibly-null String containing the name of the author.
             */
            val name: String? = null,
            /**
             * The url of the author.
             *
             * @return Possibly-null String containing the url of the author.
             */
            val url: String? = null,
            /**
             * The url of the author's icon.
             *
             * @return Possibly-null String containing the author's icon url.
             */
            val iconUrl: String? = null,
            /**
             * The url of the author's icon, proxied by Discord
             * <br></br>This url is used to access the image through Discord instead of directly to prevent ip scraping.
             *
             * @return Possibly-null String containing the proxied icon url.
             */
            @Transient
            val proxyIconUrl: String? = null
    ) {
        /**
         * Returns an [ImageProxy] for this proxied author's icon.
         *
         * @return Possibly-null [ImageProxy] of this proxied author's icon
         *
         * @see .getProxyIconUrl
         */
        val proxyIcon: ImageProxy?
            get() = proxyIconUrl?.let { ImageProxy(it) }

        override fun equals(other: Any?): Boolean {
            if (other !is AuthorInfo) return false
            return other === this || other.name == name && other.url == url && other.iconUrl == iconUrl && other.proxyIconUrl == proxyIconUrl
        }

        override fun hashCode(): Int {
            var result = name?.hashCode() ?: 0
            result = 31 * result + (url?.hashCode() ?: 0)
            result = 31 * result + (iconUrl?.hashCode() ?: 0)
            return result
        }
    }

    /**
     * Class that represents a footer at the bottom of an embed
     */
    @Serializable
    data class Footer(
            /**
             * The text in the footer
             *
             * @return Possibly-null String containing the text in the footer.
             */
            val text: String? = null,
            /**
             * The url of the footer's icon.
             *
             * @return Possibly-null String containing the footer's icon url.
             */
            val iconUrl: String? = null,
            /**
             * The url of the footer's icon, proxied by Discord
             * <br></br>This url is used to access the image through Discord instead of directly to prevent ip scraping.
             *
             * @return Possibly-null String containing the proxied icon url.
             */
            @Transient
            val proxyIconUrl: String? = null
    ) {
        /**
         * Returns an [ImageProxy] for this proxied footer's icon.
         *
         * @return Possibly-null [ImageProxy] of this proxied footer's icon
         *
         * @see .getProxyIconUrl
         */
        val proxyIcon: ImageProxy?
            get() = proxyIconUrl?.let(::ImageProxy)

        override fun equals(other: Any?): Boolean {
            if (other !is Footer) return false
            return (other === this || other.text == text && other.iconUrl == iconUrl && other.proxyIconUrl == proxyIconUrl)
        }

        override fun hashCode(): Int {
            var result = text?.hashCode() ?: 0
            result = 31 * result + (iconUrl?.hashCode() ?: 0)
            return result
        }
    }

    /**
     * Represents a field in an embed. A single embed contains an array of
     * embed fields, each with a name and value, and a boolean determining if
     * the field can display on the same line as previous fields if there is
     * enough space horizontally.
     *
     * @since  3.0
     * @author nothen
     */
    @Serializable
    data class Field constructor(
            /**
             * The name of the field
             *
             * @return Possibly-null String containing the name of the field.
             */
            val name: String? = null,
            /**
             * The value of the field
             *
             * @return Possibly-null String containing the value (contents) of the field.
             */
            val value: String? = null,
            /**
             * If the field is in line.
             *
             * @return true if the field can be in line with other fields, false otherwise.
             */
            @SerialName("inline")
            val isInline: Boolean? = null
    ) {

        init {
            require(name?.length?.let { it <= TITLE_MAX_LENGTH } == true) { "Name cannot be longer than $TITLE_MAX_LENGTH characters." }
            //if (name.isEmpty()) this.name = EmbedBuilder.ZERO_WIDTH_SPACE else this.name = name.trim { it <= ' ' }
            //if (value.isEmpty()) this.value = EmbedBuilder.ZERO_WIDTH_SPACE else this.value = value.trim { it <= ' ' }
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Field) return false
            return other === this || other.isInline == isInline && other.name == name && other.value == value
        }

        override fun hashCode(): Int {
            var result = name?.hashCode() ?: 0
            result = 31 * result + (value?.hashCode() ?: 0)
            result = 31 * result + isInline.hashCode()
            return result
        }


    }

    companion object {
        /**
         * The maximum length an embed title can have
         *
         * @see EmbedBuilder.setTitle
         * @see EmbedBuilder.addField
         */
        const val TITLE_MAX_LENGTH = 256

        /**
         * The maximum length the author name of an embed can have
         *
         * @see EmbedBuilder.setAuthor
         * @see EmbedBuilder.setAuthor
         * @see EmbedBuilder.setAuthor
         */
        const val AUTHOR_MAX_LENGTH = 256

        /**
         * The maximum length an embed field value can have
         *
         * @see EmbedBuilder.addField
         */
        const val VALUE_MAX_LENGTH = 1024

        /**
         * The maximum length the description of an embed can have
         *
         * @see EmbedBuilder.setDescription
         */
        const val DESCRIPTION_MAX_LENGTH = 4096

        /**
         * The maximum length the footer of an embed can have
         *
         * @see EmbedBuilder.setFooter
         */
        const val TEXT_MAX_LENGTH = 2048

        /**
         * The maximum length any URL can have inside an embed
         *
         * @see EmbedBuilder.setTitle
         * @see EmbedBuilder.setAuthor
         * @see EmbedBuilder.setFooter
         */
        const val URL_MAX_LENGTH = 2000

        /**
         * The maximum amount of total visible characters an embed can have
         * <br></br>This limit depends on the current [AccountType][net.dv8tion.jda.api.AccountType] and applies to BOT
         *
         * @see EmbedBuilder.setDescription
         * @see EmbedBuilder.setTitle
         * @see EmbedBuilder.setFooter
         * @see EmbedBuilder.addField
         */
        const val EMBED_MAX_LENGTH_BOT = 6000

        /**
         * The maximum amount of total visible characters an embed can have
         * <br></br>This limit depends on the current [AccountType][net.dv8tion.jda.api.AccountType] and applies to CLIENT
         *
         * @see EmbedBuilder.setDescription
         * @see EmbedBuilder.setTitle
         * @see EmbedBuilder.setFooter
         * @see EmbedBuilder.addField
         */
        const val EMBED_MAX_LENGTH_CLIENT = 2000
    }
}
