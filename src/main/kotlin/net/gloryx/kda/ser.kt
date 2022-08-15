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
    if (!it.startsWith("#")) return null
    val trimmedColor = it.removePrefix("#")
    var a: Int
    var r: Int
    var g: Int
    var b: Int
    a = 255

    if (trimmedColor.length > 7) {
        a = hexChar(trimmedColor[1]) * 16 + hexChar(trimmedColor[2])
        r = hexChar(trimmedColor[3]) * 16 + hexChar(trimmedColor[4])
        g = hexChar(trimmedColor[5]) * 16 + hexChar(trimmedColor[6])
        b = hexChar(trimmedColor[7]) * 16 + hexChar(trimmedColor[8])
    } else if (trimmedColor.length > 5) {
        r = hexChar(trimmedColor[1]) * 16 + hexChar(trimmedColor[2])
        g = hexChar(trimmedColor[3]) * 16 + hexChar(trimmedColor[4])
        b = hexChar(trimmedColor[5]) * 16 + hexChar(trimmedColor[6])
    } else if (trimmedColor.length > 4) {
        a = hexChar(trimmedColor[1])
        a += a * 16
        r = hexChar(trimmedColor[2])
        r += r * 16
        g = hexChar(trimmedColor[3])
        g += g * 16
        b = hexChar(trimmedColor[4])
        b += b * 16
    } else {
        r = hexChar(trimmedColor[1])
        r += r * 16
        g = hexChar(trimmedColor[2])
        g += g * 16
        b = hexChar(trimmedColor[3])
        b += b * 16
    }
    return Color(r, g, b, a)
}
