import net.dv8tion.jda.api.interactions.DiscordLocale
import net.gloryx.kda.markdown.i18n.GlobalTranslator
import net.gloryx.kda.markdown.i18n.Translator
import net.gloryx.oknamer.key.kinds.LangKey

object MockTranslator : Translator {
    val map = mapOf(DiscordLocale.ENGLISH_US to mapOf("test.testing" to "test2134"))
    override fun translate(key: LangKey, locale: DiscordLocale): String? = map[locale]?.get(key.asString())
}