import net.dv8tion.jda.api.interactions.DiscordLocale
import net.gloryx.kda.markdown.components.TranslationComponent
import net.gloryx.kda.markdown.i18n.Env
import net.gloryx.kda.markdown.i18n.GlobalTranslator
import net.gloryx.kda.markdown.render.TranslatableRenderer
import net.gloryx.oknamer.key.Key
import net.gloryx.oknamer.key.Namespaced
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

object TranslatableTest : Namespaced by Namespaced("test") {
    val key = Key.lang(this, "testing")
    val component = TranslationComponent(key)

    @Test
    fun `render a translatable component`() {
        GlobalTranslator.register(MockTranslator)
        val render = GlobalTranslator.renderToComponent(component, DiscordLocale.ENGLISH_US)

        println(render.toString())

        assertEquals("test2134", render.toString())
    }

    @Test
    fun `key rendering`() {
        println(key.asString())

        assertEquals("test.testing", key.asString())
    }
}