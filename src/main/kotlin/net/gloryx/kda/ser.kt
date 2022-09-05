package net.gloryx.kda

import kotlinx.serialization.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.awt.Color
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Serializer(OffsetDateTime::class)
object ODTSer : KSerializer<OffsetDateTime?> {
    override fun serialize(encoder: Encoder, value: OffsetDateTime?) {
        if (value == null) encoder.encodeNull()
        else encoder.encodeString(value.format(DateTimeFormatter.ISO_DATE_TIME))
    }

    override fun deserialize(decoder: Decoder): OffsetDateTime? = if (decoder.decodeNotNullMark()) {
        OffsetDateTime.parse(decoder.decodeString(), DateTimeFormatter.ISO_DATE_TIME)
    } else null
}

@Serializer(Color::class)
object HexSerializer : KSerializer<Color> {
    override fun deserialize(decoder: Decoder): Color = decoder.decodeString().let(::hexToColor)!!
    override fun serialize(encoder: Encoder, value: Color) {
        value.apply {
            encoder.encodeString(String.format("#%02x%02x%02x%02x", red, green, blue, alpha))
        }
    }
}

fun hexChar(c: Char): Int {
    val s_zeroChar = '0'.code
    val s_aLower = 'a'.code
    val s_aUpper = 'A'.code
    val intChar = c.code

    if ((intChar >= s_zeroChar) && (intChar <= (s_zeroChar + 9))) {
        return intChar - s_zeroChar
    }

    if ((intChar >= s_aLower) && (intChar <= (s_aLower + 5))) {
        return intChar - s_aLower + 10
    }

    if ((intChar >= s_aUpper) && (intChar <= s_aUpper + 5)) {
        return intChar - s_aUpper + 10
    }
    return 0
}

fun hexToColor(it: String): Color? {
    var hex = it
    hex = hex.replace("#", "")
    when (hex.length) {
        6 -> return Color(
            Integer.valueOf(hex.substring(0, 2), 16),
            Integer.valueOf(hex.substring(2, 4), 16),
            Integer.valueOf(hex.substring(4, 6), 16)
        )
        8 -> return Color(
            Integer.valueOf(hex.substring(0, 2), 16),
            Integer.valueOf(hex.substring(2, 4), 16),
            Integer.valueOf(hex.substring(4, 6), 16),
            Integer.valueOf(hex.substring(6, 8), 16)
        )
    }
    return null
}

