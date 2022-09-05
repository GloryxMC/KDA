package net.gloryx.kda.markdown

enum class DiscordDecoration : Decoration {
    ITALIC {
        override fun apply(text: String): String =
            text.around("_")
    },
    BOLD {
        override fun apply(text: String): String =
            text.around("**")
    },
    UNDERLINE {
        override fun apply(text: String): String =
            text.around("__")
    },
    STRIKETHROUGH {
        override fun apply(text: String): String =
            text.around("~~")
    },
    SINGLE_BLOCK_QUOTE {
        override fun apply(text: String): String = text.split('\n').reduce { acc, it -> "$acc\n> $it"}
    },
    ALL_BLOCK_QUOTE {
        override fun apply(text: String): String = ">>> $text"
    },
    SINGLE_CODE_BLOCK {
        override fun apply(text: String): String = text.split('\n').reduce { acc, it -> "$acc\n`$it`" }
    },
    MULTI_CODE_BLOCK {
        override fun apply(text: String): String = "```\n$text\n```"
    }
}

fun String.around(decoration: String) = "$decoration$this$decoration"